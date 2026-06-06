package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Enum.AuthProvider;
import com.taskmanagement.task_management_system.Enum.UserRole;
import com.taskmanagement.task_management_system.Model.dto.auth.CompleteRegistrationRequest;
import com.taskmanagement.task_management_system.Model.dto.auth.AuthResponse;
import com.taskmanagement.task_management_system.Model.dto.auth.LoginRequest;
import com.taskmanagement.task_management_system.Model.dto.auth.RefreshTokenRequest;
import com.taskmanagement.task_management_system.Model.dto.auth.RegisterRequest;
import com.taskmanagement.task_management_system.Model.dto.user.UserInfo;
import com.taskmanagement.task_management_system.Model.entity.PendingOAuthRegistration;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Service.PendingRegistrationService;
import com.taskmanagement.task_management_system.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;
    private final PendingRegistrationService pendingService;

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


    @PostMapping("/complete-registration")
    public ResponseEntity<?> completeRegistration(
            @RequestBody CompleteRegistrationRequest request
    ) {
        pendingService
                .sendVerificationEmail(
                        request.getRegistrationToken(),
                        request.getEmail()
                );
        return ResponseEntity.ok("Verification email sent");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(
            @RequestParam String token
    ) {

        PendingOAuthRegistration pending =
                pendingService.verifyEmail(token);

        Users user = Users.builder()
                .email(pending.getEmail())
                .provider(AuthProvider.GITHUB)
                .providerId(pending.getProviderId())
                .username(pending.getUsername())
                .firstName(pending.getFirstName())
                .lastName(pending.getLastName())
                .role(UserRole.USER)
                .build();

        service.save(user);
        pendingService.delete(pending);

        return ResponseEntity.ok(
                "Registration completed"
        );
    }


}