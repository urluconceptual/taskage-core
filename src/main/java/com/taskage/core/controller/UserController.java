package com.taskage.core.controller;

import com.taskage.core.dto.user.UserLoginRequestDto;
import com.taskage.core.dto.user.UserLoginResponseDto;
import com.taskage.core.dto.user.UserRegisterRequestDto;
import com.taskage.core.dto.user.UserRegisterResponseDto;
import com.taskage.core.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        String token;
        try {
            token = userService.authenticate(userLoginRequestDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserLoginResponseDto(e.getMessage()));
        }
        return ResponseEntity.ok(new UserLoginResponseDto(token));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserRegisterResponseDto> register(
            @RequestBody @Valid UserRegisterRequestDto userRegisterRequestDto) {
        try {
            userService.create(userRegisterRequestDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserRegisterResponseDto(e.getMessage()));
        }
        return ResponseEntity.ok(new UserRegisterResponseDto("Successfully registered user!"));
    }
}
