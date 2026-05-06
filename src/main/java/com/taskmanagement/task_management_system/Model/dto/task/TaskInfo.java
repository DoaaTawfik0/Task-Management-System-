package com.taskmanagement.task_management_system.Model.dto.task;

import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TaskInfo {
    private String title;

    private String description;

    private Priority priority;

    private Status status;

    private LocalDateTime dueDate;
}
