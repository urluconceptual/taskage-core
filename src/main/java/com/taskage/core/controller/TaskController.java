package com.taskage.core.controller;

import com.taskage.core.dto.task.TaskCreateRequestDto;
import com.taskage.core.dto.task.TaskUpdateRequestDto;
import com.taskage.core.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@RequestBody @Valid TaskCreateRequestDto taskCreateRequestDto) {
        taskService.create(taskCreateRequestDto);
        return ResponseEntity.ok("Task created successfully.");
    }

    @PostMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody @Valid TaskUpdateRequestDto taskUpdateRequestDto) {
        taskService.update(taskUpdateRequestDto);
        return ResponseEntity.ok("Task updated successfully.");
    }

    @DeleteMapping(path = "/delete/{taskId}")
    public ResponseEntity<String> delete(@PathVariable Integer taskId) {
        taskService.delete(taskId);
        return ResponseEntity.ok("Task deleted successfully.");
    }
}
