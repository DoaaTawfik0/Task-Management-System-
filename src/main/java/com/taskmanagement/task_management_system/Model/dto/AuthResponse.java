package com.taskmanagement.task_management_system.Model.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}