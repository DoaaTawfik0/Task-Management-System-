package com.taskmanagement.task_management_system.Model.dto.report;

import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OverdueTaskReport {

    private Long taskId;

    private String title;

    private String description;

    private Priority priority;

    private Status status;

    private LocalDateTime dueDate;

    private Long userId;

    private String firstName;
}