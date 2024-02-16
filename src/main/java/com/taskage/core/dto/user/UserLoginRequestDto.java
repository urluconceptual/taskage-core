package com.taskage.core.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class UserLoginRequestDto {
    @NotBlank(message = "Username cannot be blank")
    @Length(min = 3, max = 20, message = "Username must be between 3 and 20 characters long")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Length(min = 4, max = 50, message = "Password must be between 4 and 50 characters long")
    private String password;
}