package com.taskage.core.controller;

import com.taskage.core.dto.userActivity.UserActivityLog;
import com.taskage.core.service.UserActivityService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/core/admin")
@AllArgsConstructor
public class StatisticsController {
    private UserActivityService userActivityService;

    @GetMapping("/userActivity")
    public List<UserActivityLog> getUserActivities() {
        return userActivityService.getUserActivityLogs();
    }
}