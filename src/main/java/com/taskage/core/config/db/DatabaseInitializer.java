package com.taskage.core.config.db;

import com.taskage.core.dto.team.TeamCreateRequestDto;
import com.taskage.core.dto.user.UserRegisterRequestDto;
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
            userService.create(new UserRegisterRequestDto("admin", "Admin1!", "Admin", "Admin", "ROLE_ADMIN", null,
                    "Platform Admin", null));

            userService.create(new UserRegisterRequestDto("asmith", "ASmith123!", "Alice", "Smith", "ROLE_MANAGER",
                    null, "Software Developer", null));
            userService.create(
                    new UserRegisterRequestDto("bjohnson", "BJohnson123!", "Bob", "Johnson", "ROLE_BASIC", 2, null,
                            null));
            userService.create(new UserRegisterRequestDto("cwilliams", "CWilliams123!", "Charlie", "Williams",
                    "ROLE_BASIC", 2, null, null));
            userService.create(new UserRegisterRequestDto("djones", "DJones123!", "David", "Jones", "ROLE_BASIC", 2,
                    null, null));
            userService.create(new UserRegisterRequestDto("emiller", "EMiller123!", "Emma", "Miller", "ROLE_BASIC",
                    null, "Data Analyst", null));
            userService.create(new UserRegisterRequestDto("fbrown", "FBrown123!", "Frank", "Brown", "ROLE_MANAGER", null,
                    "Data Scientist", null));
            userService.create(
                    new UserRegisterRequestDto("gdavis", "GDavis123!", "Grace", "Davis", "ROLE_BASIC", null,
                            "Project Manager", null));
            userService.create(
                    new UserRegisterRequestDto("hwilson", "HWilson123!", "Harry", "Wilson", "ROLE_MANAGER", null,
                            "UX Designer", null));
            userService.create(new UserRegisterRequestDto("itaylor", "ITaylor123!", "Ivy", "Taylor", "ROLE_BASIC", null,
                    "UI Designer", null));
            userService.create(
                    new UserRegisterRequestDto("jthomas", "JThomas123!", "Jack", "Thomas", "ROLE_BASIC", null,
                            "QA Engineer", null));
            userService.create(new UserRegisterRequestDto("kmoore", "KMoore123!", "Kate", "Moore", "ROLE_BASIC", null,
                    "DevOps Engineer", null));
            userService.create(
                    new UserRegisterRequestDto("lmartin", "LMartin123!", "Luke", "Martin", "ROLE_BASIC", null,
                            "Network Engineer", null));
            userService.create(new UserRegisterRequestDto("nwhite", "NWhite123!", "Nina", "White", "ROLE_BASIC", null,
                    "System Administrator", null));
            userService.create(
                    new UserRegisterRequestDto("oharris", "OHarris123!", "Oscar", "Harris", "ROLE_BASIC", null,
                            "Database Administrator", null));


            teamService.create(new TeamCreateRequestDto("Development Team", 2, new ArrayList<>(List.of(2))));
            teamService.create(new TeamCreateRequestDto("Data Team", 7, new ArrayList<>(List.of(7))));
        }
    }
}
