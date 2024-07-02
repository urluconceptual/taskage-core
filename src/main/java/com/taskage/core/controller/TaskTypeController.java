package com.taskage.core.controller;

import com.taskage.core.enitity.TaskType;
import com.taskage.core.service.TaskTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/core/taskTypes")
public class TaskTypeController {
    private final TaskTypeService taskTypeService;

    @GetMapping("/getAll")
    public ResponseEntity<List<TaskType>> getAll() {
        return ResponseEntity.ok(taskTypeService.getAll());
    }
}
