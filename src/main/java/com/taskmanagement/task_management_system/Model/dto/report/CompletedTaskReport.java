package com.taskmanagement.task_management_system.Model.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompletedTaskReport {
    private Long taskId;

    private String title;

    private LocalDateTime completedAt;

    private Long userId;

    private String firstName;
}
