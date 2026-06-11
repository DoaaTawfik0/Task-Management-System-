package com.taskmanagement.task_management_system.Model.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpcomingDeadlines {
    private Long taskId;
    private String title;
    private LocalDateTime dueDate;
    private Long daysRemaining;


}
