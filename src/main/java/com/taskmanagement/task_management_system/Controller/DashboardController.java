package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Model.dto.dashboard.*;
import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Model.dto.team.TeamInfo;
import com.taskmanagement.task_management_system.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("dashboard/")
public class DashboardController {
    private final DashboardService service;

    @GetMapping("summary")
    public ResponseEntity<DashboardResponse> summary(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(service.summary(currentUser.user().getId()));
    }
    @GetMapping("my-teams")
    public ResponseEntity<List<TeamInfo>> teams(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(service.myTeams(currentUser.user().getId()));
    }
    @GetMapping("my-tasks")
    public ResponseEntity<List<TaskInfo>> tasks(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(service.myTasks(currentUser.user().getId()));
    }
    @GetMapping("tasks-by-status")
    public ResponseEntity<TasksByStatus> tasksByStatus(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(service.getTasksStatus(currentUser.user().getId()));
    }
    @GetMapping("task-by-priority/{priority}")
    public ResponseEntity<List<TaskByPriority>> taskByPriority(@AuthenticationPrincipal CustomUserDetails currentUser , @PathVariable("priority")Priority priority) {
        return ResponseEntity.ok(service.getTaskByPriority(currentUser.user().getId() , priority));
    }
    @GetMapping("recent-completed")
    public ResponseEntity<List<TaskInfo>> recentCompleted(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(service.recentCompleted(currentUser.user().getId()));
    }
    @GetMapping("upcoming-deadlines")
    public ResponseEntity<List<UpcomingDeadlines>> upcomingDeadlines(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(service.upcomingDeadlines(currentUser.user().getId()));
    }
    @GetMapping("pending-requests")
    public ResponseEntity<PendingRequests> pendingRequests(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(service.countRequests(currentUser.user().getId()));
    }

}
