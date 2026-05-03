package com.taskmanagement.task_management_system.Model.dto.task;

import com.taskmanagement.task_management_system.Enum.Status;

public record UpdateStatusRequest(Status status) {
}
