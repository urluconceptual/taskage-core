package com.taskage.core.config.db;

import com.taskage.core.dto.sprint.SprintCreateRequestDto;
import com.taskage.core.dto.task.TaskCreateRequestDto;
import com.taskage.core.dto.team.TeamSaveRequestDto;
import com.taskage.core.dto.user.UserRegisterRequestDto;
import com.taskage.core.enitity.JobTitle;
import com.taskage.core.enitity.Priority;
import com.taskage.core.enitity.Status;
import com.taskage.core.enitity.TaskType;
import com.taskage.core.repository.PriorityRepository;
import com.taskage.core.repository.StatusRepository;
import com.taskage.core.repository.UserRepository;
import com.taskage.core.service.SprintService;
import com.taskage.core.service.TaskService;
import com.taskage.core.service.TeamService;
import com.taskage.core.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Configuration
@Profile("!test")
@AllArgsConstructor
public class DatabaseInitializer {

    private UserRepository userRepository;
    private UserService userService;
    private TeamService teamService;
    private SprintService sprintService;
    private StatusRepository statusRepository;
    private PriorityRepository priorityRepository;
    private TaskService taskService;

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            userService.create(new UserRegisterRequestDto("admin", "Admin1!", "Admin",
                    "Admin", "ROLE_ADMIN", JobTitle.builder().name(
                    "Platform Admin").build(), null));

            userService.create(new UserRegisterRequestDto("asmith", "ASmith123!", "Alice",
                    "Smith", "ROLE_MANAGER", JobTitle.builder().name("Software Developer").build(), null));
            userService.create(
                    new UserRegisterRequestDto("bjohnson", "BJohnson123!", "Bob",
                            "Johnson", "ROLE_BASIC", JobTitle.builder().id(2).build(),
                            null));
            userService.create(new UserRegisterRequestDto("cwilliams", "CWilliams123!", "Charlie",
                    "Williams",
                    "ROLE_BASIC", JobTitle.builder().id(2).build(), null));
            userService.create(new UserRegisterRequestDto("djones", "DJones123!", "David",
                    "Jones", "ROLE_BASIC", JobTitle.builder().id(2).build(),
                    null));
            userService.create(new UserRegisterRequestDto("emiller", "EMiller123!", "Emma", "Miller", "ROLE_BASIC",
                    JobTitle.builder().name("Data Analyst").build(), null));
            userService.create(
                    new UserRegisterRequestDto("fbrown", "FBrown123!", "Frank",
                            "Brown", "ROLE_MANAGER",
                            JobTitle.builder().name("Data Scientist").build(), null));
            userService.create(
                    new UserRegisterRequestDto("gdavis", "GDavis123!", "Grace",
                            "Davis", "ROLE_BASIC",
                            JobTitle.builder().name("Project Manager").build(), null));
            userService.create(
                    new UserRegisterRequestDto("hwilson", "HWilson123!", "Harry", "Wilson",
                            "ROLE_MANAGER", JobTitle.builder().name("UX Designer").build(), null));
            userService.create(new UserRegisterRequestDto("itaylor", "ITaylor123!", "Ivy",
                    "Taylor", "ROLE_BASIC", JobTitle.builder().name("UI Designer").build(), null));
            userService.create(
                    new UserRegisterRequestDto("jthomas", "JThomas123!", "Jack", "Thomas",
                            "ROLE_BASIC", JobTitle.builder().name("QA Engineer").build(), null));
            userService.create(new UserRegisterRequestDto("kmoore", "KMoore123!", "Kate",
                    "Moore", "ROLE_BASIC", JobTitle.builder().name("DevOps Engineer").build(), null));
            userService.create(
                    new UserRegisterRequestDto("lmartin", "LMartin123!", "Luke", "Martin",
                            "ROLE_BASIC", JobTitle.builder().name("Network Engineer").build(),
                            null));
            userService.create(new UserRegisterRequestDto("nwhite", "NWhite123!", "Nina", "White", "ROLE_BASIC",
                    JobTitle.builder().name("System Administrator").build(),
                    null));
            userService.create(
                    new UserRegisterRequestDto("oharris", "OHarris123!", "Oscar",
                            "Harris", "ROLE_BASIC", JobTitle.builder().name("Database Administrator").build()
                            , null));

            teamService.create(
                    new TeamSaveRequestDto(null, "Development Team", 2, new ArrayList<>(List.of(2, 3, 4, 5))));
            teamService.create(new TeamSaveRequestDto(null, "Data Team", 7, new ArrayList<>(List.of(7))));

            Calendar date1 = Calendar.getInstance();
            date1.set(2024, Calendar.APRIL, 6);
            Calendar date2 = Calendar.getInstance();
            date2.set(2024, Calendar.APRIL, 20);
            Calendar date3 = Calendar.getInstance();
            date2.set(2024, Calendar.APRIL, 21);
            Calendar date4 = Calendar.getInstance();
            date3.set(2024, Calendar.APRIL, 30);
            sprintService.create(new SprintCreateRequestDto(1, date1, date2));
            sprintService.create(new SprintCreateRequestDto(1, date2, date3));

            statusRepository.save(Status.builder().name("To Do").build());
            statusRepository.save(Status.builder().name("In Progress").build());
            statusRepository.save(Status.builder().name("Done").build());

            priorityRepository.save(Priority.builder().name("Low").build());
            priorityRepository.save(Priority.builder().name("Medium").build());
            priorityRepository.save(Priority.builder().name("High").build());

            TaskType taskType1 = TaskType.builder().name("Development").build();
            TaskType taskType2 = TaskType.builder().name("Design").build();
            TaskType taskType3 = TaskType.builder().name("Testing").build();

            taskService.create(new TaskCreateRequestDto("Implement login functionality",
                    "Develop the login functionality including UI and backend API integration.", 1, 8, 2, 2, taskType1,
                    5));
            taskService.create(new TaskCreateRequestDto("Fix bug in user profile page",
                    "Resolve the issue causing incorrect data display in the user profile page.", 2, 7, 2, 3, taskType2,
                    7));
            taskService.create(new TaskCreateRequestDto("Code review for recent commits",
                    "Perform code review for the latest commits in the repository.", 1, 10, 2, 3, taskType3, 2));
            taskService.create(new TaskCreateRequestDto("Update project documentation",
                    "Update the project documentation to reflect recent changes and new features.", 1, 8, 2, 4,
                    taskType1, 5));
            taskService.create(new TaskCreateRequestDto("Design new dashboard UI",
                    "Create a new design for the dashboard interface based on the latest user feedback.", 2, 7, 2, 4,
                    taskType2, 7));
            taskService.create(
                    new TaskCreateRequestDto("Clarify requirements", "Talk to stakeholders about requirements.", 1, 10,
                            2, 5, taskType3, 2));

            taskService.create(new TaskCreateRequestDto("Develop Registration Feature",
                    "Create the registration functionality with UI design and backend API connection.", 1, 8, 1, 2,
                    taskType1, 5));

            taskService.create(new TaskCreateRequestDto("Resolve Data Mismatch Issue",
                    "Fix the bug causing data mismatch in the user profile summary section.", 2, 7, 1, 3, taskType2,
                    7));

            taskService.create(new TaskCreateRequestDto("Review Code for Latest Updates",
                    "Conduct a comprehensive review of the recent updates and commits in the codebase.", 1, 10, 1, 3,
                    taskType3, 2));

            taskService.create(new TaskCreateRequestDto("Revise Project Documentation",
                    "Amend the project documentation to include recent updates and additional features.", 1, 8, 1, 4,
                    taskType1, 5));

            taskService.create(new TaskCreateRequestDto("Create New Dashboard Design",
                    "Design a revamped dashboard interface incorporating recent user feedback.", 2, 7, 1, 4,
                    taskType2, 7));

            taskService.create(new TaskCreateRequestDto("Gather Requirement Details",
                    "Discuss with stakeholders to gather and clarify project requirements.", 1, 10, 1, 5, taskType3,
                    2));
        }
    }
}
