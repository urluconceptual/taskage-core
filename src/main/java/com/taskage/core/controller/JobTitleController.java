package com.taskage.core.controller;

import com.taskage.core.enitity.JobTitle;
import com.taskage.core.service.JobTitleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/jobTitles")
public class JobTitleController {
    private final JobTitleService jobTitleService;

    @GetMapping("/getAll")
    public List<JobTitle> getAll() {
        return jobTitleService.getAll();
    }
}
