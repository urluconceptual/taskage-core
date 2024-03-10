package com.taskage.core.dto.task;

import jakarta.validation.constraints.NotNull;

public record TaskUpdateRequestDto(@NotNull Integer id, @NotNull String name, @NotNull String description,
                                   @NotNull Integer statusId, @NotNull Integer priorityId, @NotNull Integer sprintId,
                                   @NotNull Integer assigneeId) {
}
