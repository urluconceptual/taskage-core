package com.taskage.core.dto.user;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserRegisterRequestDto(@NotBlank(message = "Username cannot be blank") @Length(min = 3, max = 20,
        message = "Username must be between 3 and 20 characters long") String username,
                                     @NotBlank(message = "Password cannot be blank") @Length(min = 4, max = 50,
                                             message = "Password must be between 4 and 50 characters long") String password,
                                     @NotBlank(message = "First name cannot be blank") String firstName,
                                     @NotBlank(message = "Last name cannot be blank") String lastName,
                                     @NotBlank(message = "Level of authorization is required") String authRole,
                                     Integer jobTitleId, String newJobTitleName) {
}
