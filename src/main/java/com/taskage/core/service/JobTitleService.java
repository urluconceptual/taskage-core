package com.taskage.core.service;

import com.taskage.core.enitity.JobTitle;
import com.taskage.core.repository.JobTitleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class JobTitleService {
    private final JobTitleRepository jobTitleRepository;

    public List<JobTitle> getAll() {
        return jobTitleRepository.findAll();
    }
}
