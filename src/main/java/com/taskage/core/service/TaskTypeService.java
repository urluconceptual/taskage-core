package com.taskage.core.service;

import com.taskage.core.enitity.TaskType;
import com.taskage.core.repository.TaskTypeRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskTypeService {
    private final TaskTypeRepository taskTypeRepository;

    @NotNull
    public List<TaskType> getAll() {
        return taskTypeRepository.findAll();
    }
}
