package com.taskage.core.service;

import com.taskage.core.config.security.JwtProvider;
import com.taskage.core.dto.user.*;
import com.taskage.core.enitity.JobTitle;
import com.taskage.core.enitity.Team;
import com.taskage.core.enitity.User;
import com.taskage.core.exception.conflict.UsernameConflictException;
import com.taskage.core.exception.notFound.NotFoundException;
import com.taskage.core.exception.security.AuthenticationFailedException;
import com.taskage.core.exception.security.UnauthorizedUserException;
import com.taskage.core.mapper.UserMapper;
import com.taskage.core.repository.JobTitleRepository;
import com.taskage.core.repository.TeamRepository;
import com.taskage.core.repository.UserRepository;
import com.taskage.core.utils.UserActivityLogger;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final JobTitleRepository jobTitleRepository;
    private final TeamRepository teamRepository;
    private final UserMapper userMapper;
    private final UserActivityLogger userActivityLogger;
    @Value("${jwt.ttl}")
    private Long timeToLive;

    @NotNull
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::mapUserToUserResponseDto).toList();
    }

    @NotNull
    public List<UserResponseDto> getAllForTeam(@NotNull Integer teamId) {
        return userRepository.findAllByTeamId(teamId).stream().map(userMapper::mapUserToUserResponseDto).toList();
    }

    @NotNull
    public UserResponseDto get(@NotNull Integer id) {
        return userRepository.findById(id).map(userMapper::mapUserToUserResponseDto)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @NotNull
    public UserLoginResponseDto authenticate(@NotNull UserLoginRequestDto userLoginRequestDto)
            throws NotFoundException, UnauthorizedUserException {
        User user = getUserByUsername(userLoginRequestDto.username());

        if (!passwordEncoder.matches(userLoginRequestDto.password(), user.getPassword())) {
            userActivityLogger.logUserActivity("Failed login attempt. Incorrect password.", "ERROR");
            throw new AuthenticationFailedException();
        }

        userActivityLogger.logUserActivity("Successful login - " + userLoginRequestDto.username(), "INFO");
        return new UserLoginResponseDto(userMapper.mapUserToUserResponseDto(user),
                jwtProvider.generateToken(user.getId().toString(), timeToLive, user.getAuthRole()));
    }

    @NotNull
    public UserResponseDto create(@NotNull UserRegisterRequestDto userRegisterRequestDto)
            throws UsernameConflictException {
        if (userRepository.existsByUsername(userRegisterRequestDto.username())) {
            throw new UsernameConflictException();
        }

        final String encodedPassword = passwordEncoder.encode(userRegisterRequestDto.password());
        User newUser = userMapper.mapUserCreateEditDtoToUser(userRegisterRequestDto, encodedPassword);

        userRepository.save(newUser);
        assignJobTitleToUser(newUser, userRegisterRequestDto.jobTitle());

        if (userRegisterRequestDto.teamId() != null) {
            Team team = teamRepository.getById(userRegisterRequestDto.teamId());
            assignTeamToUser(newUser, team);
        }

        userActivityLogger.logUserActivity("User created with username " + newUser.getUsername(), "INFO");
        return userMapper.mapUserToUserResponseDto(newUser);
    }

    public UserResponseDto update(@NotNull UserUpdateRequestDto userUpdateRequestDto) {
        User user = getUserById(userUpdateRequestDto.id());
        if (userRepository.existsByUsername(userUpdateRequestDto.username())) {
            throw new UsernameConflictException();
        }
        userMapper.mapUserUpdateDtoToUser(user, userUpdateRequestDto);
        userRepository.save(user);

        if (userUpdateRequestDto.teamId() != null) {
            Team team = teamRepository.getById(userUpdateRequestDto.teamId());
            assignTeamToUser(user, team);
        }

        assignJobTitleToUser(user, userUpdateRequestDto.jobTitle());

        userActivityLogger.logUserActivity("User updated with id " + user.getId(), "INFO");
        return userMapper.mapUserToUserResponseDto(user);
    }

    public void delete(@NotNull Integer userId) {
        userRepository.deleteById(userId);
        userActivityLogger.logUserActivity("User deleted with id " + userId, "INFO");
    }

    public void assignJobTitleToUser(@NotNull User user, @NotNull JobTitle jobTitle) {
        JobTitle assignedJobTitle;
        if (jobTitle.getId() == null && jobTitleRepository.findByName(jobTitle.getName()) == null) {
            assignedJobTitle = jobTitleRepository.save(jobTitle);
        } else {
            assignedJobTitle = jobTitle.getId() != null ? jobTitleRepository.findById(jobTitle.getId()).get() :
                    jobTitleRepository.findByName(jobTitle.getName());
        }

        user.setJobTitle(assignedJobTitle);
        userRepository.save(user);
        userActivityLogger.logUserActivity("Assigned job title to user with id " + user.getId(), "INFO");
    }

    public void assignTeamToUser(@NotNull User user, @NotNull Team team) {
        if (user.getTeam() != null && user.getTeam().getId().equals(team.getId())) {
            return;
        }
        user.setTeam(team);
        userRepository.save(user);
        userActivityLogger.logUserActivity("Assigned team to user with id " + user.getId(), "INFO");
    }

    public void assignTeamToAll(@NotNull List<Integer> userIds, @NotNull Team newTeam) {
        userRepository.findAllById(userIds).forEach(user -> {
            user.setTeam(newTeam);
            userRepository.save(user);
            userActivityLogger.logUserActivity("Assigned team to user with id " + user.getId(), "INFO");
        });
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }
}