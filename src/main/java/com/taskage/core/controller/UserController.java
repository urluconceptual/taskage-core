package com.taskage.core.controller;

import com.taskage.core.dto.user.*;
import com.taskage.core.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private UserService userService;

    @GetMapping(path = "/checkLocalCredentials")
    public ResponseEntity<String> checkLocalCredentials() {
        return ResponseEntity.ok("Credentials are valid!");
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        return ResponseEntity.ok(userService.authenticate(userLoginRequestDto));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserRegisterResponseDto> register(
            @RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) {
        userService.create(userRegisterRequestDto);
        return ResponseEntity.ok(new UserRegisterResponseDto("Successfully registered user!"));
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<UserResponseDto> get(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.get(id));
    }
}
