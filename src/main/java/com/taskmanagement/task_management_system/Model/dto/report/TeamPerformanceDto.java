package com.taskmanagement.task_management_system.Model.dto.report;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamPerformanceDto {

    private Long teamId;

    private String teamName;

    private long totalTasks;

    private long completedTasks;

    private long pendingTasks;

    private long overdueTasks;

    private double completionRate;

    private List<MemberPerformanceDto> members;
}