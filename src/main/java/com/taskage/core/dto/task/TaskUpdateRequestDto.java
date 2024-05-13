package com.taskage.core.dto.task;

import jakarta.validation.constraints.NotNull;

public record TaskUpdateRequestDto(@NotNull Integer id, @NotNull String title, @NotNull String description,
                                   @NotNull Integer statusId, @NotNull Integer priorityId, @NotNull Integer sprintId,
                                   @NotNull Integer estimation, @NotNull Integer progress,
                                   @NotNull Integer assigneeId) {
}
