package com.taskmanagement.task_management_system.Model.dto.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}