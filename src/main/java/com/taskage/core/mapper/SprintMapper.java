package com.taskage.core.mapper;

import com.taskage.core.dto.sprint.SprintCreateRequestDto;
import com.taskage.core.enitity.Sprint;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class SprintMapper {
    public Sprint mapSprintCreateRequestDtoToSprint(SprintCreateRequestDto sprintCreateRequestDto) {
        return Sprint.builder().startDate(sprintCreateRequestDto.startDate()).endDate(sprintCreateRequestDto.endDate())
                .build();
    }
}
