package com.taskage.core.mapper;

import com.taskage.core.dto.user.UserRegisterRequestDto;
import com.taskage.core.enitity.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserMapper {
    public User mapUserRegisterDtoToUser(UserRegisterRequestDto userRegisterRequestDto, String encodedPassword) {
        return new User(
                userRegisterRequestDto.username(),
                encodedPassword,
                userRegisterRequestDto.firstName(),
                userRegisterRequestDto.lastName(),
                userRegisterRequestDto.authRole()
        );
    }
}
