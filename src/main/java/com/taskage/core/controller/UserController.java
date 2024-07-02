package com.taskage.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskage.core.config.websocket.UserWebSocketHandler;
import com.taskage.core.dto.user.*;
import com.taskage.core.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/core/users")
public class UserController {
    private final UserService userService;
    private final UserWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(path = "/checkLocalCredentials")
    public ResponseEntity<String> checkLocalCredentials() {
        return ResponseEntity.ok("Credentials are valid!");
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        return ResponseEntity.ok(userService.authenticate(userLoginRequestDto));
    }

    @GetMapping(path = "/getAllForTeam/{teamId}")
    public ResponseEntity<List<UserResponseDto>> getAllForTeam(@PathVariable Integer teamId) {
        return ResponseEntity.ok(userService.getAllForTeam(teamId));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserResponseDto> register(
            @RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) throws IOException {
        UserResponseDto userResponseDto = userService.create(userRegisterRequestDto);
        webSocketHandler.broadcastMessage(
                "{\"action\": \"ADD\", \"user\": " + objectMapper.writeValueAsString(userResponseDto) + "}");
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping(path = "/update")
    public ResponseEntity<UserResponseDto> update(
            @RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto) throws IOException {
        UserResponseDto userResponseDto = userService.update(userUpdateRequestDto);
        webSocketHandler.broadcastMessage(
                "{\"action\": \"UPDATE\", \"user\": " + objectMapper.writeValueAsString(userResponseDto) + "}");
        return ResponseEntity.ok(userResponseDto);
    }

    @DeleteMapping(path = "/delete/{userId}")
    public ResponseEntity<String> delete(@PathVariable Integer userId) throws IOException {
        userService.delete(userId);
        webSocketHandler.broadcastMessage(
                "{\"action\": \"DELETE\", \"userId\": " + objectMapper.writeValueAsString(userId) + "}");
        return ResponseEntity.ok("Successfully deleted user!");
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
