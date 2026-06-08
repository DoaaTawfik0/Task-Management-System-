package com.taskmanagement.task_management_system.Model.dto.report;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberPerformanceDto {

    private Long userId;

    private String username;

    private long assignedTasks;

    private long completedTasks;

    private long pendingTasks;

    private long overdueTasks;

    private double completionRate;
}