package com.taskage.core.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TeamSaveRequestDto(Integer id,
                                 @NotBlank String name,
                                 @NotNull Integer teamLeadId,
                                 @NotNull List<Integer> teamMemberIds) {
}
