package com.taskage.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskage.core.config.websocket.TeamWebSocketHandler;
import com.taskage.core.dto.team.TeamResponseDto;
import com.taskage.core.dto.team.TeamSaveRequestDto;
import com.taskage.core.service.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core/teams")
public class TeamController {
    private final TeamService teamService;
    private final TeamWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(path = "/create")
    @NotNull
    public ResponseEntity<String> create(@RequestBody @Valid TeamSaveRequestDto teamSaveRequestDto) throws IOException {
        TeamResponseDto savedTeam = teamService.create(teamSaveRequestDto);
        webSocketHandler.broadcastMessage(
                "{\"action\": \"ADD\", \"team\": " + objectMapper.writeValueAsString(savedTeam) + "}");
        return ResponseEntity.ok("Successfully created team!");
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody @Valid TeamSaveRequestDto teamSaveRequestDto) throws IOException {
        TeamResponseDto updatedTeam = teamService.update(teamSaveRequestDto);
        webSocketHandler.broadcastMessage(
                "{\"action\": \"UPDATE\", \"team\": " + objectMapper.writeValueAsString(updatedTeam) + "}");
        return ResponseEntity.ok("Successfully updated team!");
    }

    @DeleteMapping(path = "/delete/{teamId}")
    public ResponseEntity<String> delete(@PathVariable Integer teamId) throws IOException {
        teamService.delete(teamId);
        webSocketHandler.broadcastMessage("{\"action\": \"DELETE\", \"teamId\": " + teamId + "}");
        return ResponseEntity.ok("Successfully deleted team!");
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<TeamResponseDto>> getAll() {
        return ResponseEntity.ok(teamService.getAll());
    }
}