package com.taskage.core.dto.user;

import com.taskage.core.enitity.JobTitle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserUpdateRequestDto(Integer id,
                                   @NotBlank @Length(min = 3, max = 20) String username,
                                   @Length(min = 4, max = 50) String password,
                                   @NotBlank String firstName,
                                   @NotBlank String lastName,
                                   @NotBlank String authRole,
                                   @NotNull JobTitle jobTitle, Integer teamId) {
}
