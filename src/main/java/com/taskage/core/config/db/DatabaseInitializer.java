package com.taskage.core.config.db;

import com.taskage.core.dto.team.TeamSaveRequestDto;
import com.taskage.core.dto.user.UserRegisterRequestDto;
import com.taskage.core.enitity.JobTitle;
import com.taskage.core.repository.UserRepository;
import com.taskage.core.service.TeamService;
import com.taskage.core.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
public class DatabaseInitializer {

    private UserRepository userRepository;
    private UserService userService;
    private TeamService teamService;

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
                    "Jones", "ROLE_BASIC", JobTitle.builder().id(2).build(), null));
            userService.create(new UserRegisterRequestDto("emiller", "EMiller123!", "Emma", "Miller", "ROLE_BASIC",
                    JobTitle.builder().name("Data Analyst").build(), null));
            userService.create(
                    new UserRegisterRequestDto("fbrown", "FBrown123!", "Frank",
                            "Brown", "ROLE_MANAGER", JobTitle.builder().name("Data Scientist").build(), null));
            userService.create(
                    new UserRegisterRequestDto("gdavis", "GDavis123!", "Grace",
                            "Davis", "ROLE_BASIC", JobTitle.builder().name("Project Manager").build(), null));
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
                            "ROLE_BASIC", JobTitle.builder().name("Network Engineer").build(), null));
            userService.create(new UserRegisterRequestDto("nwhite", "NWhite123!", "Nina", "White", "ROLE_BASIC",
                    JobTitle.builder().name("System Administrator").build(), null));
            userService.create(
                    new UserRegisterRequestDto("oharris", "OHarris123!", "Oscar",
                            "Harris", "ROLE_BASIC", JobTitle.builder().name("Database Administrator").build()
                            , null));


            teamService.create(new TeamSaveRequestDto(null, "Development Team", 2, new ArrayList<>(List.of(2))));
            teamService.create(new TeamSaveRequestDto(null, "Data Team", 7, new ArrayList<>(List.of(7))));
        }
    }
}
