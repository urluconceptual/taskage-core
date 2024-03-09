package com.taskage.core.dto.user;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(@NotBlank @Length(min = 3, max = 20) String username,
                                  @NotBlank @Length(min = 4, max = 50) String password) {
}