package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.dto.AuthResponse;
import com.taskmanagement.task_management_system.Model.dto.RefreshTokenRequest;
import com.taskmanagement.task_management_system.Model.dto.UserInfo;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public Users register(@Valid @RequestBody UserInfo user) {
        return service.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid@RequestBody UserInfo user) {
        return service.login(user);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return service.refresh(request);
    }

    @PostMapping("/auth/logout")
    public void logout(@Valid @RequestBody RefreshTokenRequest request) {
        service.logout(request);
    }
}