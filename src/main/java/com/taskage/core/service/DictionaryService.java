package com.taskage.core.service;

import com.taskage.core.enitity.Priority;
import com.taskage.core.enitity.Status;
import com.taskage.core.repository.PriorityRepository;
import com.taskage.core.repository.StatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DictionaryService {
    private final PriorityRepository priorityRepository;
    private final StatusRepository statusRepository;

    public Map<Integer, String> getStatuses() {
        return statusRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Status::getId, Status::getName));
    }

    public Map<Integer, String> getPriorities() {
        return priorityRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Priority::getId, Priority::getName));
    }
}
