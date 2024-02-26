package com.taskage.core.dto.user;

public record UserLoginResponseDto(String username, String firstName, String lastName, String authRole, String token) {
}
