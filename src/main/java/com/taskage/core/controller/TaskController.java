package com.taskage.core.controller;

import com.taskage.core.dto.task.TaskCreateRequestDto;
import com.taskage.core.dto.task.TaskUpdateRequestDto;
import com.taskage.core.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(TaskCreateRequestDto taskCreateRequestDto) {
        taskService.create(taskCreateRequestDto);
        return ResponseEntity.ok("Task created successfully.");
    }

    @PostMapping(path = "/update")
    public ResponseEntity<String> update(TaskUpdateRequestDto taskUpdateRequestDto) {
        taskService.update(taskUpdateRequestDto);
        return ResponseEntity.ok("Task updated successfully.");
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> delete(Integer taskId) {
        taskService.delete(taskId);
        return ResponseEntity.ok("Task deleted successfully.");
    }
}
