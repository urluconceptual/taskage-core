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
                .title(taskCreateRequestDto.title())
                .description(taskCreateRequestDto.description())
                .statusId(1)
                .estimation(taskCreateRequestDto.estimation())
                .progress(0)
                .priorityId(taskCreateRequestDto.priorityId())
                .build();
    }
}
