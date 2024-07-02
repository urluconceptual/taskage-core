package com.taskage.core.dto.task;

import com.taskage.core.enitity.TaskType;
import jakarta.validation.constraints.NotNull;

public record TaskCreateRequestDto(@NotNull String title, @NotNull String description,
                                   @NotNull Integer priorityId, @NotNull Integer estimation,
                                   @NotNull Integer sprintId, @NotNull Integer assigneeId,
                                   @NotNull TaskType taskType, @NotNull Integer effortPoints) {
}