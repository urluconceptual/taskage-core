package com.taskage.core.service;

import com.taskage.core.config.security.JwtProvider;
import com.taskage.core.dto.user.UserLoginRequestDto;
import com.taskage.core.dto.user.UserRegisterRequestDto;
import com.taskage.core.enitity.JobTitle;
import com.taskage.core.enitity.User;
import com.taskage.core.exception.UnauthorizedUserException;
import com.taskage.core.exception.notFound.UserNotFoundException;
import com.taskage.core.exception.conflict.UsernameConflictException;
import com.taskage.core.mapper.UserMapper;
import com.taskage.core.repository.JobTitleRepository;
import com.taskage.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final Long TTL = 5L;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final JobTitleRepository jobTitleRepository;
    private UserMapper userMapper;

    public String authenticate(UserLoginRequestDto userLoginRequestDto)
            throws UserNotFoundException, UnauthorizedUserException {
        User user = userRepository
                .findByUsername(userLoginRequestDto.username())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(userLoginRequestDto.password(), user.getPassword())) {
            throw new UnauthorizedUserException();
        }

        return jwtProvider.generateToken(user.getUsername(), TTL, user.getAuthRole());
    }

    public void create(UserRegisterRequestDto userRegisterRequestDto) throws UsernameConflictException {
        userRepository.findByUsername(userRegisterRequestDto.username()).ifPresent(username -> {
            throw new UsernameConflictException();
        });

        final String encodedPassword = passwordEncoder.encode(userRegisterRequestDto.password());

        User newUser = userMapper.mapUserRegisterDtoToUser(userRegisterRequestDto, encodedPassword);
        JobTitle jobTitle = userRegisterRequestDto.jobTitleId() != null &&
                jobTitleRepository.findById(userRegisterRequestDto.jobTitleId()).isPresent() ?
                jobTitleRepository.findById(userRegisterRequestDto.jobTitleId()).get() :
                new JobTitle(userRegisterRequestDto.newJobTitleName());

        newUser.setJobTitle(jobTitle);

        userRepository.save(newUser);
    }
}