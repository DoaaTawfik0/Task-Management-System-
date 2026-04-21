package com.taskmanagement.task_management_system.Exception;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime date, String message, String description) {
}
