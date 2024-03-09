package com.taskage.core.dto.user;

import com.taskage.core.enitity.JobTitle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserUpdateRequestDto(Integer id,
                                   @NotBlank(message = "Username cannot be blank") @Length(min = 3, max = 20,
                                           message = "Username must be between 3 and 20 characters long") String username,
                                   @Length(min = 4, max = 50,
                                           message = "Password must be between 4 and 50 characters long") String password,
                                   @NotBlank(message = "First name cannot be blank") String firstName,
                                   @NotBlank(message = "Last name cannot be blank") String lastName,
                                   @NotBlank(message = "Level of authorization is required") String authRole,
                                   @NotNull(message = "Job title is required") JobTitle jobTitle, Integer teamId) {
}
