package com.taskage.core.dto.userActivity;

import java.time.LocalDateTime;

public record UserActivityLog(long userId, String level, String activity, LocalDateTime timestamp) {
}