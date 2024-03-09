package com.taskage.core.dto.sprint;

import jakarta.validation.constraints.NotNull;

import java.util.Calendar;

public record SprintCreateRequestDto(@NotNull Integer teamId, @NotNull Calendar startDate, @NotNull Calendar endDate) {
}