package com.taskage.core.controller;

import com.taskage.core.dto.team.TeamCrudResponseDto;
import com.taskage.core.dto.team.TeamResponseDto;
import com.taskage.core.dto.team.TeamSaveRequestDto;
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
    public ResponseEntity<TeamCrudResponseDto> create(@RequestBody @Valid TeamSaveRequestDto teamSaveRequestDto) {
        teamService.create(teamSaveRequestDto);
        return ResponseEntity.ok(new TeamCrudResponseDto("Successfully created team!"));
    }

    @PutMapping(path = "/update")
    public ResponseEntity<TeamCrudResponseDto> update(@RequestBody @Valid TeamSaveRequestDto teamSaveRequestDto) {
        teamService.update(teamSaveRequestDto);
        return ResponseEntity.ok(new TeamCrudResponseDto("Successfully updated team!"));
    }

    @DeleteMapping(path = "/delete/{teamId}")
    public ResponseEntity<TeamCrudResponseDto> delete(@PathVariable Integer teamId) {
        teamService.delete(teamId);
        return ResponseEntity.ok(new TeamCrudResponseDto("Successfully deleted team!"));
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<TeamResponseDto>> getAll() {
        return ResponseEntity.ok(teamService.getAll());
    }
}
