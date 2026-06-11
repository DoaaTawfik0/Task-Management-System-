package com.taskmanagement.task_management_system.Model.dto.dashboard;

import com.taskmanagement.task_management_system.Enum.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskByPriority {
    private Long id;
    private String title;
    private Priority priority;
    private LocalDateTime dueDate;
}
