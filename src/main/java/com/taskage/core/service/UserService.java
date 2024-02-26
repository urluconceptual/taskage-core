package com.taskage.core.service;

import com.taskage.core.config.security.JwtProvider;
import com.taskage.core.dto.user.UserLoginRequestDto;
import com.taskage.core.dto.user.UserLoginResponseDto;
import com.taskage.core.dto.user.UserRegisterRequestDto;
import com.taskage.core.dto.user.UserResponseDto;
import com.taskage.core.enitity.JobTitle;
import com.taskage.core.enitity.Team;
import com.taskage.core.enitity.User;
import com.taskage.core.exception.UnauthorizedUserException;
import com.taskage.core.exception.conflict.UsernameConflictException;
import com.taskage.core.exception.notFound.UserNotFoundException;
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
    private UserMapper userMapper;
    private final TeamRepository teamRepository;

    public UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto)
            throws UserNotFoundException, UnauthorizedUserException {
        User user = userRepository
                .findByUsername(userLoginRequestDto.username())
                .orElseThrow(UserNotFoundException::new);

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

        User newUser = userMapper.mapUserRegisterDtoToUser(userRegisterRequestDto, encodedPassword);
        JobTitle jobTitle;
        if (userRegisterRequestDto.jobTitleId() != null && jobTitleRepository.existsById(userRegisterRequestDto.jobTitleId()))
                jobTitle = jobTitleRepository.findById(userRegisterRequestDto.jobTitleId()).get();
        else {
            jobTitle = JobTitle.builder().name(userRegisterRequestDto.newJobTitleName()).build();
            jobTitleRepository.save(jobTitle);
        }

        newUser.setJobTitle(jobTitle);

        if(userRegisterRequestDto.teamId() != null && teamRepository.existsById(userRegisterRequestDto.teamId())) {
            newUser.setTeam(new Team(userRegisterRequestDto.teamId()));
        }

        userRepository.save(newUser);
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
        return userRepository.findById(id).map(userMapper::mapUserToUserResponseDto).orElseThrow(UserNotFoundException::new);
    }
}