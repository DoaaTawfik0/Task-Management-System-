package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Model.dto.dashboard.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TaskService taskService;
    private final TeamService teamService;

    public DashboardResponse summary(Long userId) {
        return DashboardResponse
                .builder()
                .assignedTasks(taskService.getMyTasks(userId).stream().count())
                .completedTasks(taskService.countCompletedTasks(userId))
                .inProgressTasks(taskService.countInProgressTasks(userId))
                .overdueTasks(taskService.getOverdueTasks(LocalDateTime.now()))
                .todoTasks(taskService.countToDoTasks(userId))
                .teamsCount(teamService.countTeams(userId))
                .build();
    }
}
