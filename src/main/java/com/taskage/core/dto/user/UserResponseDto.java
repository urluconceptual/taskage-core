package com.taskage.core.dto.user;

import com.taskage.core.dto.jobTitle.JobTitleResponseDto;
import com.taskage.core.dto.team.TeamResponseDto;

public record UserResponseDto(Integer id, String username, String firstName, String lastName, String authRole,
                              JobTitleResponseDto jobTitle, TeamResponseDto team) {
}