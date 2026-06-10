package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Model.dto.dashboard.DashboardResponse;
import com.taskmanagement.task_management_system.Model.dto.dashboard.UpcomingDeadlines;
import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Model.dto.team.TeamInfo;
import com.taskmanagement.task_management_system.Repository.TeamRepository;
import com.taskmanagement.task_management_system.Repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TaskService taskService;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;

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
    public List<TeamInfo> myTeams(Long userId) {
        return teamRepository.findByUsersId(userId);
    }
    public List<TaskInfo> myTasks(Long userId) {
        return taskRepository.getMyTasks(userId);
    }
}
