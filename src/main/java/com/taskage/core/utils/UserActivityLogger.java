package com.taskage.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserActivityLogger {
    public void logUserActivity(String activity, String level) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication == null ? "NOT KNOWN" : authentication.getName();
        log.info("User Activity: {{\"userId\": {}, \"level\": \"{}\", \"activity\": \"{}\", \"timestamp\": \"{}\"}}",
                userId, level, activity, java.time.LocalDateTime.now());
    }
}
