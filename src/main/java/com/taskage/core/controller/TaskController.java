package com.taskage.core.controller;

import com.taskage.core.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TaskController {
    private final TaskRepository taskRepository;
}
