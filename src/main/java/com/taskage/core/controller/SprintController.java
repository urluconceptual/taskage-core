package com.taskage.core.controller;

import com.taskage.core.dto.sprint.SprintCreateRequestDto;
import com.taskage.core.dto.sprint.SprintUpdateRequestDto;
import com.taskage.core.enitity.Sprint;
import com.taskage.core.service.SprintService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/sprints")
public class SprintController {
    private SprintService sprintService;

    @GetMapping(path = "/getAllForTeam/{teamId}")
    public ResponseEntity<List<Sprint>> getAllForTeam(@PathVariable Integer teamId) {
        return ResponseEntity.ok(sprintService.getAllForTeam(teamId));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@RequestBody @Valid SprintCreateRequestDto sprintCreateRequestDto) {
        sprintService.create(sprintCreateRequestDto);
        return ResponseEntity.ok("Successfully created sprint!");
    }

    @PostMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody @Valid SprintUpdateRequestDto sprintUpdateRequestDto) {
        sprintService.update(sprintUpdateRequestDto);
        return ResponseEntity.ok("Successfully updated sprint!");
    }

    @DeleteMapping(path = "/delete/{sprintId}")
    public ResponseEntity<String> delete(@PathVariable Integer sprintId) {
        sprintService.delete(sprintId);
        return ResponseEntity.ok("Successfully deleted sprint!");
    }
}