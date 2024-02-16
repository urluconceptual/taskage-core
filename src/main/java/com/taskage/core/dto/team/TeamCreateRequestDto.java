package com.taskage.core.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamCreateRequestDto(@NotBlank(message = "Team name is required.") String name,
                                   @NotNull(message = "Team lead is required.") Integer teamLeadId) {
}
