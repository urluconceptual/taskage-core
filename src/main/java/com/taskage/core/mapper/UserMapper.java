package com.taskage.core.mapper;

import com.taskage.core.dto.user.UserRegisterRequestDto;
import com.taskage.core.dto.user.UserResponseDto;
import com.taskage.core.enitity.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserMapper {
    public User mapUserRegisterDtoToUser(UserRegisterRequestDto userRegisterRequestDto, String encodedPassword) {
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
}
