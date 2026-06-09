package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Enum.Status;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Model.dto.report.*;
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

    public OverdueTasksResponse getOverdueTasks() {

        List<OverdueTaskReport> tasks =
                taskRepository.findOverdueTasks(LocalDateTime.now());

        return new OverdueTasksResponse(
                tasks.size(),
                tasks
        );
    }

    public CompletedTasksResponse getCompletedTasks() {

        List<CompletedTaskReport> tasks =
                taskRepository.findCompletedTasks();

        return new CompletedTasksResponse(
                tasks.size(),
                tasks
        );
    }

    public TeamPerformanceDto getTeamPerformance(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));

        List<Task> teamTasks = team.getTasks();

        long totalTasks = teamTasks.size();

        long completedTasks = teamTasks.stream()
                .filter(task -> task.getStatus() == Status.COMPLETED)
                .count();

        long pendingTasks = teamTasks.stream()
                .filter(task -> task.getStatus() != Status.COMPLETED)
                .count();

        long overdueTasks = teamTasks.stream()
                .filter(task ->
                        task.getDueDate().isBefore(LocalDateTime.now())
                                && task.getStatus() != Status.COMPLETED)
                .count();

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

    private MemberPerformanceDto buildMemberPerformance(
            Users user
    ) {

        long assignedTasks = user.getTasks().size();

        long completedTasks =
                user.getTasks()
                        .stream()
                        .filter(task ->
                                task.getStatus() == Status.COMPLETED)
                        .count();

        long pendingTasks =
                user.getTasks()
                        .stream()
                        .filter(task ->
                                task.getStatus() != Status.COMPLETED)
                        .count();

        long overdueTasks =
                user.getTasks()
                        .stream()
                        .filter(task ->
                                task.getDueDate().isBefore(LocalDateTime.now())
                                        && task.getStatus() != Status.COMPLETED)
                        .count();

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