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
    public ResponseEntity<String> register(
            @RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) {
        userService.create(userRegisterRequestDto);
        return ResponseEntity.ok("Successfully registered user!");
    }

    @PostMapping(path = "/update")
    public ResponseEntity<String> update(
            @RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto) {
        userService.update(userUpdateRequestDto);
        return ResponseEntity.ok("Successfully updated user!");
    }

    @DeleteMapping(path = "/delete/{userId}")
    public ResponseEntity<String> delete(@PathVariable Integer userId) {
        userService.delete(userId);
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
