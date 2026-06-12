package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import com.taskmanagement.task_management_system.Mapper.TaskMapper;
import com.taskmanagement.task_management_system.Model.dto.dashboard.*;
import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Model.dto.team.TeamInfo;
import com.taskmanagement.task_management_system.Model.entity.Task;
import com.taskmanagement.task_management_system.Repository.PendingTeamRepository;
import com.taskmanagement.task_management_system.Repository.TeamRepository;
import com.taskmanagement.task_management_system.Repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TaskService taskService;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper mapper;
    private final PendingTeamRepository pendingTeamRepository;

    public DashboardResponse summary(Long userId) {
        return DashboardResponse
                .builder()
                .assignedTasks((long) taskService.getMyTasks(userId).size())
                .completedTasks(taskService.countTasks(userId , Status.COMPLETED))
                .inProgressTasks(taskService.countTasks(userId , Status.IN_PROGRESS))
                .overdueTasks(taskService.getUserOverdueTasks(userId,LocalDateTime.now()))
                .todoTasks(taskService.countTasks(userId , Status.TO_DO))
                .teamsCount(teamService.countTeams(userId))
                .build();
    }
    public List<TeamInfo> myTeams(Long userId) {
        return teamRepository.findByUsersId(userId);
    }
    public List<TaskInfo> myTasks(Long userId) {
        return taskRepository.getMyTasks(userId);
    }


    public TasksByStatus getTasksStatus(Long userId) {

        return TasksByStatus.builder()
                .todo(taskService.countTasks(userId, Status.TO_DO))
                .inProgress(taskService.countTasks(userId, Status.IN_PROGRESS))
                .completed(taskService.countTasks(userId, Status.COMPLETED))
                .build();
    }

    public List<TaskByPriority> getTaskByPriority(Long userId , Priority priority) {
        return taskRepository.findByUserIdAndPriority(userId , priority);
    }

    public List<TaskInfo> recentCompleted(Long userId) {
        return taskRepository.findTop5ByUsersIdAndStatusOrderByIdDesc(userId , Status.COMPLETED);
    }
    public List<UpcomingDeadlines> upcomingDeadlines(Long userId) {

        List<Task> tasks = taskRepository.findByUsersIdAndDueDateAfter(userId , LocalDateTime.now());
        List<UpcomingDeadlines> deadlines= mapper.toUpcomingDeadlines(tasks);

        for(UpcomingDeadlines d: deadlines) {
            if(ChronoUnit.DAYS.between(LocalDate.now(), d.getDueDate()) > 0) {
                d.setRemaining("Days : " + ChronoUnit.DAYS.between(LocalDateTime.now(), d.getDueDate()));
            }else {
                d.setRemaining("Hours: " + ChronoUnit.HOURS.between(LocalDateTime.now() , d.getDueDate()));
            }
        }

        return deadlines;
    }
    public PendingRequests countRequests(Long userId){
        return PendingRequests
                .builder()
                .pendingRequests(pendingTeamRepository.CountAllByUserId(userId))
                .build();
    }

}
