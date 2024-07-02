package com.taskage.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskage.core.config.websocket.SprintWebSocketHandler;
import com.taskage.core.dto.sprint.SprintCreateRequestDto;
import com.taskage.core.dto.sprint.SprintUpdateRequestDto;
import com.taskage.core.enitity.Sprint;
import com.taskage.core.service.SprintService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/core/sprints")
public class SprintController {
    private final SprintWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private SprintService sprintService;

    @GetMapping(path = "/getAllForTeam/{teamId}")
    public ResponseEntity<List<Sprint>> getAllForTeam(@PathVariable Integer teamId) {
        return ResponseEntity.ok(sprintService.getAllForTeam(teamId));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@RequestBody @Valid SprintCreateRequestDto sprintCreateRequestDto)
            throws IOException {
        Sprint sprint = sprintService.create(sprintCreateRequestDto);
        webSocketHandler.broadcastMessage(
                "{\"action\": \"ADD\", \"sprint\": " + objectMapper.writeValueAsString(sprint) + "}");
        return ResponseEntity.ok("Successfully created sprint!");
    }

    @PostMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody @Valid SprintUpdateRequestDto sprintUpdateRequestDto)
            throws IOException {
        Sprint sprint = sprintService.update(sprintUpdateRequestDto);
        webSocketHandler.broadcastMessage(
                "{\"action\": \"UPDATE\", \"sprint\": " + objectMapper.writeValueAsString(sprint) + "}");
        return ResponseEntity.ok("Successfully updated sprint!");
    }

    @DeleteMapping(path = "/delete/{sprintId}")
    public ResponseEntity<String> delete(@PathVariable Integer sprintId) throws IOException {
        sprintService.delete(sprintId);
        webSocketHandler.broadcastMessage("{\"action\": \"DELETE\", \"sprintId\": " + sprintId + "}");
        return ResponseEntity.ok("Successfully deleted sprint!");
    }
}