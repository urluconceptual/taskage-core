package com.taskage.core.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class UserRegisterRequestDto {
    @NotBlank(message = "Username cannot be blank")
    @Length(min = 3, max = 20, message = "Username must be between 3 and 20 characters long")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Length(min = 4, max = 50, message = "Password must be between 4 and 50 characters long")
    private String password;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "Level of authorization is required")
    private String authRole;

    private Integer jobTitleId;

    private String newJobTitleName;
}
