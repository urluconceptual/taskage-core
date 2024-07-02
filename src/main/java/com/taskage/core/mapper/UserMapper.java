package com.taskage.core.mapper;

import com.taskage.core.dto.user.UserRegisterRequestDto;
import com.taskage.core.dto.user.UserResponseDto;
import com.taskage.core.dto.user.UserUpdateRequestDto;
import com.taskage.core.enitity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User mapUserCreateEditDtoToUser(UserRegisterRequestDto userRegisterRequestDto, String encodedPassword) {
        return User.builder()
                .username(userRegisterRequestDto.username())
                .password(encodedPassword)
                .firstName(userRegisterRequestDto.firstName())
                .lastName(userRegisterRequestDto.lastName())
                .authRole(userRegisterRequestDto.authRole())
                .build();
    }

    public UserResponseDto mapUserToUserResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getAuthRole(),
                new JobTitleMapper().mapJobTitleToJobTitleResponseDto(user.getJobTitle()),
                user.getTeam() != null ? new TeamMapper().mapTeamToTeamResponseDto(user.getTeam()) : null
        );
    }

    public void mapUserUpdateDtoToUser(User user, UserUpdateRequestDto userUpdateRequestDto) {
        user.setUsername(userUpdateRequestDto.username());
        user.setFirstName(userUpdateRequestDto.firstName());
        user.setLastName(userUpdateRequestDto.lastName());
        user.setAuthRole(userUpdateRequestDto.authRole());
        Optional.ofNullable(userUpdateRequestDto.password())
                .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
    }
}
