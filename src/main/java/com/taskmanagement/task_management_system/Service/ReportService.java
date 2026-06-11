package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Enum.Status;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Model.dto.report.*;
import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Model.entity.Task;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.TeamRepository;
import com.taskmanagement.task_management_system.Repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final UserService userService;

    public OverdueTasksResponse<OverdueTaskReport> getOverdueTasks() {

        List<OverdueTaskReport> tasks =
                taskRepository.findOverdueTasks(LocalDateTime.now());

        return new OverdueTasksResponse<>(
                tasks.size(),
                tasks
        );
    }

    public CompletedTasksResponse<CompletedTaskReport> getCompletedTasks() {

        List<CompletedTaskReport> tasks =
                taskRepository.findCompletedTasks();

        return new CompletedTasksResponse<>(
                tasks.size(),
                tasks
        );
    }

    public TeamPerformanceDto getTeamPerformance(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));

        List<Task> teamTasks = team.getTasks();

        long totalTasks = teamTasks.size();

        long completedTasks = getCompletedTasks(teamTasks);

        long pendingTasks = getPendingTasks(teamTasks);

        long overdueTasks = getOverdueTasks(teamTasks);

        double completionRate =
                totalTasks == 0
                        ? 0
                        : ((double) completedTasks / totalTasks) * 100;

        List<MemberPerformanceDto> members =
                team.getUsers()
                        .stream()
                        .map(this::buildMemberPerformance)
                        .toList();

        return TeamPerformanceDto.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .pendingTasks(pendingTasks)
                .overdueTasks(overdueTasks)
                .completionRate(completionRate)
                .members(members)
                .build();
    }


    public OverdueTasksResponse<TaskInfo> getCurrentUserOverdueTasks(Long userId) {

        Users user = userService.getUserEntity(userId);

        List<TaskInfo> overdueTasks = user.getTasks().stream().filter(task -> task.getStatus() != Status.COMPLETED)
                .filter(task -> task.getDueDate().isBefore(LocalDateTime.now()))
                .map(TaskInfo::new)
                .toList();

        return new OverdueTasksResponse<>(overdueTasks.size(), overdueTasks);
    }

    public CompletedTasksResponse<TaskInfo> getCurrentUserCompletedTasks(Long userId) {

        Users user = userService.getUserEntity(userId);

        List<TaskInfo> completedTasks = user.getTasks().stream().filter(task -> task.getStatus() == Status.COMPLETED)
                .map(TaskInfo::new)
                .toList();

        return new CompletedTasksResponse<>(completedTasks.size(), completedTasks);
    }

    public MemberPerformanceDto getUserPerformance(Long userId) {

        Users user = userService.getUserEntity(userId);

        return buildMemberPerformance(user);
    }


    private static long getCompletedTasks(List<Task> teamTasks) {
        return teamTasks.stream()
                .filter(task -> task.getStatus() == Status.COMPLETED)
                .count();
    }

    private static long getOverdueTasks(List<Task> teamTasks) {
        return teamTasks.stream()
                .filter(task ->
                        task.getDueDate().isBefore(LocalDateTime.now())
                                && task.getStatus() != Status.COMPLETED)
                .count();
    }

    private static long getPendingTasks(List<Task> teamTasks) {
        return teamTasks.stream()
                .filter(task -> task.getStatus() != Status.COMPLETED)
                .count();
    }

    private MemberPerformanceDto buildMemberPerformance(
            Users user
    ) {

        long assignedTasks = user.getTasks().size();

        List<Task> userTasks = user.getTasks().stream().toList();

        long completedTasks = getCompletedTasks(userTasks);

        long pendingTasks = getPendingTasks(userTasks);

        long overdueTasks = getOverdueTasks(userTasks);

        double completionRate =
                assignedTasks == 0
                        ? 0
                        : ((double) completedTasks / assignedTasks) * 100;

        return MemberPerformanceDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .assignedTasks(assignedTasks)
                .completedTasks(completedTasks)
                .pendingTasks(pendingTasks)
                .overdueTasks(overdueTasks)
                .completionRate(completionRate)
                .build();
    }
}