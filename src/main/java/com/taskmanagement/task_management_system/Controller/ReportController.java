package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Model.dto.report.*;
import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/overdue")
    public OverdueTasksResponse<OverdueTaskReport> getOverdueTasks() {
        return reportService.getOverdueTasks();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/completed")
    public CompletedTasksResponse<CompletedTaskReport> getCompletedTasks() {
        return reportService.getCompletedTasks();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/team/{teamId}")
    public TeamPerformanceDto getTeamPerformance(
            @PathVariable Long teamId
    ) {
        return reportService.getTeamPerformance(teamId);
    }

    @GetMapping("/overdue/me")
    public OverdueTasksResponse<TaskInfo> getCurrentUserOverdueTasks(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.user().getId();

        return reportService.getCurrentUserOverdueTasks(userId);
    }

    @GetMapping("/completed/me")
    public CompletedTasksResponse<TaskInfo> getCurrentUserCompletedTasks(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.user().getId();

        return reportService.getCurrentUserCompletedTasks(userId);
    }

    @GetMapping("/performance/me")
    public MemberPerformanceDto getCurrentUserPerformance(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.user().getId();

        return reportService.getUserPerformance(userId);
    }
}