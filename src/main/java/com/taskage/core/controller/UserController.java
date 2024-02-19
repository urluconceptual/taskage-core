package com.taskage.core.controller;

import com.taskage.core.dto.user.UserLoginRequestDto;
import com.taskage.core.dto.user.UserLoginResponseDto;
import com.taskage.core.dto.user.UserRegisterRequestDto;
import com.taskage.core.dto.user.UserRegisterResponseDto;
import com.taskage.core.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private UserService userService;

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        String token = userService.authenticate(userLoginRequestDto);
        return ResponseEntity.ok(new UserLoginResponseDto(token));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserRegisterResponseDto> register(
            @RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) {
        userService.create(userRegisterRequestDto);
        return ResponseEntity.ok(new UserRegisterResponseDto("Successfully registered user!"));
    }
}
