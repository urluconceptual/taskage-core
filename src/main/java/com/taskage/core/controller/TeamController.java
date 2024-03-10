package com.taskage.core.controller;

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
    public ResponseEntity<String> create(@RequestBody @Valid TeamSaveRequestDto teamSaveRequestDto) {
        teamService.create(teamSaveRequestDto);
        return ResponseEntity.ok("Successfully created team!");
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody @Valid TeamSaveRequestDto teamSaveRequestDto) {
        teamService.update(teamSaveRequestDto);
        return ResponseEntity.ok("Successfully updated team!");
    }

    @DeleteMapping(path = "/delete/{teamId}")
    public ResponseEntity<String> delete(@PathVariable Integer teamId) {
        teamService.delete(teamId);
        return ResponseEntity.ok("Successfully deleted team!");
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<TeamResponseDto>> getAll() {
        return ResponseEntity.ok(teamService.getAll());
    }
}
