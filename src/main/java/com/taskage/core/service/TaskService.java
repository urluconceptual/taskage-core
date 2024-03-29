package com.taskage.core.service;

import com.taskage.core.dto.task.TaskCreateRequestDto;
import com.taskage.core.dto.task.TaskUpdateRequestDto;
import com.taskage.core.enitity.Task;
import com.taskage.core.mapper.TaskMapper;
import com.taskage.core.repository.SprintRepository;
import com.taskage.core.repository.TaskRepository;
import com.taskage.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;

    public void create(TaskCreateRequestDto taskCreateRequestDto) {
        Task task = taskMapper.mapTaskCreateRequestDtoToTask(taskCreateRequestDto);
        taskRepository.save(task);
        addToSprint(task, taskCreateRequestDto.sprintId());
        assignToUser(task, taskCreateRequestDto.assigneeId());
    }

    public void addToSprint(Task task, Integer sprintId) {
        task.setSprint(
                sprintRepository.findById(sprintId).orElseThrow(() -> new RuntimeException("Sprint not found.")));
        taskRepository.save(task);
    }

    public void assignToUser(Task task, Integer userId) {
        task.setAssignee(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found.")));
        taskRepository.save(task);
    }

    public void update(TaskUpdateRequestDto taskUpdateRequestDto) {
        Task task = taskRepository.findById(taskUpdateRequestDto.id())
                .orElseThrow(() -> new RuntimeException("Task not found."));
        task.setName(taskUpdateRequestDto.name());
        task.setDescription(taskUpdateRequestDto.description());
        task.setStatusId(taskUpdateRequestDto.statusId());
        task.setPriorityId(taskUpdateRequestDto.priorityId());
        taskRepository.save(task);
        addToSprint(task, taskUpdateRequestDto.sprintId());
        assignToUser(task, taskUpdateRequestDto.assigneeId());
    }

    public void delete(Integer taskId) {
        taskRepository.deleteById(taskId);
    }
}