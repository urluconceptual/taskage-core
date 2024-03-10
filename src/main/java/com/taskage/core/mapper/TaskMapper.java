package com.taskage.core.mapper;

import com.taskage.core.dto.task.TaskCreateRequestDto;
import com.taskage.core.enitity.Task;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TaskMapper {
    public Task mapTaskCreateRequestDtoToTask(TaskCreateRequestDto taskCreateRequestDto) {
        return Task.builder()
                   .name(taskCreateRequestDto.name())
                   .description(taskCreateRequestDto.description())
                   .statusId(taskCreateRequestDto.statusId())
                   .priorityId(taskCreateRequestDto.priorityId())
                   .build();
    }
}