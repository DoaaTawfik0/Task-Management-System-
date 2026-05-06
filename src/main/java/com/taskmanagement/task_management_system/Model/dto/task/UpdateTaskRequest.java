package com.taskmanagement.task_management_system.Model.dto.task;

import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateTaskRequest {
    @Size(min = 5, max = 50)
    private String title;
    @Size(min = 10, max = 250)
    private String description;

    private Priority priority;

    private Status status;

    private LocalDateTime dueDate;
}
