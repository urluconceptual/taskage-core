package com.taskage.core.service;

import com.taskage.core.config.security.JwtProvider;
import com.taskage.core.dto.user.*;
import com.taskage.core.enitity.JobTitle;
import com.taskage.core.enitity.Team;
import com.taskage.core.enitity.User;
import com.taskage.core.exception.UnauthorizedUserException;
import com.taskage.core.exception.conflict.UsernameConflictException;
import com.taskage.core.exception.notFound.NotFoundException;
import com.taskage.core.mapper.UserMapper;
import com.taskage.core.repository.JobTitleRepository;
import com.taskage.core.repository.TeamRepository;
import com.taskage.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final Long TTL = 8L;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final JobTitleRepository jobTitleRepository;
    private final TeamRepository teamRepository;
    private UserMapper userMapper;

    public UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto)
            throws NotFoundException, UnauthorizedUserException {
        User user = userRepository
                .findByUsername(userLoginRequestDto.username())
                .orElseThrow(() -> new NotFoundException("User with username " +
                                                                 userLoginRequestDto.username() +
                                                                 " not found"));

        if (!passwordEncoder.matches(userLoginRequestDto.password(), user.getPassword())) {
            throw new UnauthorizedUserException();
        }

        return new UserLoginResponseDto(user.getUsername(),
                                        user.getFirstName(),
                                        user.getLastName(),
                                        user.getAuthRole(),
                                        jwtProvider.generateToken(user.getUsername(), TTL, user.getAuthRole()));
    }

    public void create(UserRegisterRequestDto userRegisterRequestDto) throws UsernameConflictException {
        userRepository.findByUsername(userRegisterRequestDto.username()).ifPresent(username -> {
            throw new UsernameConflictException();
        });

        final String encodedPassword = passwordEncoder.encode(userRegisterRequestDto.password());

        User newUser = userMapper.mapUserCreateEditDtoToUser(userRegisterRequestDto, encodedPassword);

        userRepository.save(newUser);

        assignJobTitleToUser(newUser.getId(), userRegisterRequestDto.jobTitle());

        if (userRegisterRequestDto.teamId() != null) {
            assignTeamToUser(newUser.getId(), userRegisterRequestDto.teamId());
        }
    }

    public void assignJobTitleToUser(Integer userId, JobTitle jobTitle) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        if (jobTitle.getId() != null) {
            user.setJobTitle(jobTitleRepository.getById(jobTitle.getId()));
        } else {
            jobTitleRepository.save(jobTitle);
            user.setJobTitle(jobTitle);
        }
        userRepository.save(user);
    }

    public void assignTeamToUser(Integer userId, Integer teamId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Team team = teamRepository.findById(teamId)
                                  .orElseThrow(() -> new NotFoundException("Team with id " + teamId + " not found"));
        user.setTeam(team);
        userRepository.save(user);
    }

    public void update(UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(userUpdateRequestDto.id())
                                  .orElseThrow(() -> new NotFoundException("User with id " + userUpdateRequestDto.id() + " not found"));

        user.setUsername(userUpdateRequestDto.username());
        user.setFirstName(userUpdateRequestDto.firstName());
        user.setLastName(userUpdateRequestDto.lastName());
        user.setAuthRole(userUpdateRequestDto.authRole());
        if (userUpdateRequestDto.password() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateRequestDto.password()));
        }

        userRepository.save(user);

        if (userUpdateRequestDto.teamId() != null) {
            assignTeamToUser(user.getId(), userUpdateRequestDto.teamId());
        }

        assignJobTitleToUser(user.getId(), userUpdateRequestDto.jobTitle());
    }

    public void assignTeamToAll(List<Integer> userIds, Team newTeam) {
        userRepository.findAllById(userIds).forEach(user -> {
            user.setTeam(newTeam);
            userRepository.save(user);
        });
    }

    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::mapUserToUserResponseDto).toList();
    }

    public UserResponseDto get(Integer id) {
        return userRepository.findById(id).map(userMapper::mapUserToUserResponseDto)
                             .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    public void delete(Integer userId) {
        userRepository.deleteById(userId);
    }
}