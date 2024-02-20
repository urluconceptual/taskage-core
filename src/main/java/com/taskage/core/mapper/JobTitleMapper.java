package com.taskage.core.mapper;

import com.taskage.core.dto.jobTitle.JobTitleResponseDto;
import com.taskage.core.enitity.JobTitle;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class JobTitleMapper {
    public JobTitleResponseDto mapJobTitleToJobTitleResponseDto(JobTitle jobTitle) {
        return new JobTitleResponseDto(
                jobTitle.getId(),
                jobTitle.getName()
        );
    }
}
