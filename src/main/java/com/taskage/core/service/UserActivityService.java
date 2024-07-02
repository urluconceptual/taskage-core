package com.taskage.core.service;

import com.taskage.core.dto.userActivity.UserActivityLog;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserActivityService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    @Value("${logging.file.name}")
    private String LOG_FILE_PATH;

    @NotNull
    public List<UserActivityLog> getUserActivityLogs() {
        List<UserActivityLog> userActivityLogs = new ArrayList<>();
        Pattern pattern = Pattern.compile(
                "User Activity: \\{\\{\"userId\": (\\d+), \"level\": \"(.+?)\", \"activity\": \"(.+?)\", " +
                        "\"timestamp\": \"(.+?)\"\\}\\}");

        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    UserActivityLog log = new UserActivityLog(Long.parseLong(matcher.group(1)),
                            matcher.group(2),
                            matcher.group(3),
                            LocalDateTime.parse(matcher.group(4), DATE_TIME_FORMATTER));
                    userActivityLogs.add(log);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userActivityLogs;
    }
}
