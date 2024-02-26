package com.taskage.core.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TeamCreateRequestDto(@NotBlank(message = "Team name is required.") String name,
                                   @NotNull(message = "Team lead is required.") Integer teamLeadId,
                                   @NotNull(message = "Team members list is required.") List<Integer> teamMemberIds) {
}
