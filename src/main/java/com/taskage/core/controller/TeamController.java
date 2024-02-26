package com.taskage.core.controller;

import com.taskage.core.dto.team.TeamCreateRequestDto;
import com.taskage.core.dto.team.TeamCreateResponseDto;
import com.taskage.core.dto.team.TeamResponseDto;
import com.taskage.core.service.TeamService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;

    @PostMapping(path = "/create")
    public ResponseEntity<TeamCreateResponseDto> create(@RequestBody @Valid TeamCreateRequestDto teamCreateRequestDto) {
        teamService.create(teamCreateRequestDto);
        return ResponseEntity.ok(new TeamCreateResponseDto("Successfully created team!"));
    }

    // get all teams
    @GetMapping(path = "/getAll")
    public ResponseEntity<List<TeamResponseDto>> getAll() {
        return ResponseEntity.ok(teamService.getAll());
    }
}
