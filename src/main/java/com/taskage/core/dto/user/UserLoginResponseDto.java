package com.taskage.core.dto.user;

public record UserLoginResponseDto(UserResponseDto user, String token) {
}
