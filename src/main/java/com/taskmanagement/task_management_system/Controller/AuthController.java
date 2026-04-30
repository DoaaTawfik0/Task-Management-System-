package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.dto.auth.AuthResponse;
import com.taskmanagement.task_management_system.Model.dto.auth.LoginRequest;
import com.taskmanagement.task_management_system.Model.dto.auth.RefreshTokenRequest;
import com.taskmanagement.task_management_system.Model.dto.auth.RegisterRequest;
import com.taskmanagement.task_management_system.Model.dto.user.UserInfo;
import com.taskmanagement.task_management_system.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;

    @PostMapping("/register")
    public UserInfo register(@Valid @RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return service.refresh(request);
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody RefreshTokenRequest request) {
        service.logout(request);
    }
}