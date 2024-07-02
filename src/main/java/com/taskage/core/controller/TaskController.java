package com.taskage.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskage.core.config.websocket.TaskWebSocketHandler;
import com.taskage.core.dto.task.TaskCreateRequestDto;
import com.taskage.core.dto.task.TaskUpdateRequestDto;
import com.taskage.core.enitity.Task;
import com.taskage.core.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/core/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@RequestBody @Valid TaskCreateRequestDto taskCreateRequestDto)
            throws IOException {
        Task task = taskService.create(taskCreateRequestDto);
        webSocketHandler.broadcastMessage(
                "{\"action\": \"ADD\", \"task\": " + objectMapper.writeValueAsString(task) + "}");
        return ResponseEntity.ok("Task created successfully.");
    }

    @PostMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody @Valid TaskUpdateRequestDto taskUpdateRequestDto)
            throws IOException {
        Task task = taskService.update(taskUpdateRequestDto);
        webSocketHandler.broadcastMessage(
                "{\"action\": \"UPDATE\", \"task\": " + objectMapper.writeValueAsString(task) + "}");
        return ResponseEntity.ok("Task updated successfully.");
    }

    @DeleteMapping(path = "/delete/{taskId}")
    public ResponseEntity<String> delete(@PathVariable Integer taskId) throws IOException {
        taskService.delete(taskId);
        webSocketHandler.broadcastMessage("{\"action\": \"DELETE\", \"taskId\": " + taskId + "}");
        return ResponseEntity.ok("Task deleted successfully.");
    }
}
