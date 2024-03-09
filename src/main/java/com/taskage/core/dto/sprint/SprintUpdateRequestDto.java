package com.taskage.core.dto.sprint;

import jakarta.validation.constraints.NotNull;

import java.util.Calendar;

public record SprintUpdateRequestDto(@NotNull Integer id, Calendar startDate, Calendar endDate) {
}
