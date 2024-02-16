package com.taskage.core.config.db;

import com.taskage.core.dto.user.UserRegisterRequestDto;
import com.taskage.core.repository.UserRepository;
import com.taskage.core.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class DatabaseInitializer {

    private UserRepository userRepository;
    private UserService userService;

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            userService.create(new UserRegisterRequestDto("admin", "Admin1!", "Admin", "Admin", "ROLE_ADMIN", null,
                    "Platform Admin"));
        }
    }
}
