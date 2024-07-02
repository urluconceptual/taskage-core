package com.taskage.core.service;

import com.taskage.core.dto.task.TaskCreateRequestDto;
import com.taskage.core.dto.task.TaskUpdateRequestDto;
import com.taskage.core.enitity.Task;
import com.taskage.core.enitity.TaskType;
import com.taskage.core.mapper.TaskMapper;
import com.taskage.core.repository.SprintRepository;
import com.taskage.core.repository.TaskRepository;
import com.taskage.core.repository.TaskTypeRepository;
import com.taskage.core.repository.UserRepository;
import com.taskage.core.utils.UserActivityLogger;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;
    private final TaskTypeRepository taskTypeRepository;
    private final UserActivityLogger userActivityLogger;

    @NotNull
    public Task create(@NotNull TaskCreateRequestDto taskCreateRequestDto) {
        Task task = taskMapper.mapTaskCreateRequestDtoToTask(taskCreateRequestDto);
        taskRepository.save(task);
        addToSprint(task, taskCreateRequestDto.sprintId());
        assignToUser(task, taskCreateRequestDto.assigneeId());
        assignTaskType(task, taskCreateRequestDto.taskType());
        userActivityLogger.logUserActivity("Task created with id " + task.getId(), "INFO");
        return task;
    }

    private void assignTaskType(Task task, @NotNull TaskType taskType) {
        if (taskType.getId() != null) {
            task.setTaskType(taskTypeRepository.findById(taskType.getId()).get());
        } else {
            taskTypeRepository.save(taskType);
            task.setTaskType(taskType);
        }
        taskRepository.save(task);
        userActivityLogger.logUserActivity("Assigned task type to task with id " + task.getId(), "INFO");
    }

    private void addToSprint(@NotNull Task task, @NotNull Integer sprintId) {
        task.setSprint(
                sprintRepository.findById(sprintId).orElseThrow(() -> new RuntimeException("Sprint not found.")));
        taskRepository.save(task);
        userActivityLogger.logUserActivity("Added task with id " + task.getId() + " to sprint with id " + sprintId,
                "INFO");
    }

    private void assignToUser(@NotNull Task task, @NotNull Integer userId) {
        task.setAssignee(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found.")));
        taskRepository.save(task);
        userActivityLogger.logUserActivity("Assigned task with id " + task.getId() + " to user with id " + userId,
                "INFO");
    }

    @NotNull
    public Task update(@NotNull TaskUpdateRequestDto taskUpdateRequestDto) {
        Task task = taskRepository.findById(taskUpdateRequestDto.id())
                .orElseThrow(() -> new RuntimeException("Task not found."));
        task.setTitle(taskUpdateRequestDto.title());
        task.setDescription(taskUpdateRequestDto.description());
        task.setStatusId(taskUpdateRequestDto.statusId());
        task.setPriorityId(taskUpdateRequestDto.priorityId());
        task.setEstimation(taskUpdateRequestDto.estimation());
        task.setProgress(taskUpdateRequestDto.progress());
        taskRepository.save(task);
        addToSprint(task, taskUpdateRequestDto.sprintId());
        assignToUser(task, taskUpdateRequestDto.assigneeId());
        assignTaskType(task, taskUpdateRequestDto.taskType());
        userActivityLogger.logUserActivity("Task updated with id " + task.getId(), "INFO");
        return task;
    }

    public void delete(@NotNull Integer taskId) {
        taskRepository.deleteById(taskId);
        userActivityLogger.logUserActivity("Task deleted with id " + taskId, "INFO");
    }
}