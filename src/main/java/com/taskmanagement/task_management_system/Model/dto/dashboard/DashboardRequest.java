package com.taskmanagement.task_management_system.Model.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardRequest {
    private Long assignedTasks;
    private Long completedTasks;
    private Long inProgressTasks;
    private Long todoTasks;
    private Long overdueTasks;
    private Long teamsCount;

}
