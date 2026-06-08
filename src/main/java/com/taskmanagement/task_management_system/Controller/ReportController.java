package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.dto.report.CompletedTasksResponse;
import com.taskmanagement.task_management_system.Model.dto.report.OverdueTasksResponse;
import com.taskmanagement.task_management_system.Model.dto.report.TeamPerformanceDto;
import com.taskmanagement.task_management_system.Service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/overdue")
    public OverdueTasksResponse getOverdueTasks() {
        return reportService.getOverdueTasks();
    }

    @GetMapping("/completed")
    public CompletedTasksResponse getCompletedTasks() {
        return reportService.getCompletedTasks();
    }

    @GetMapping("/team/{teamId}")
    public TeamPerformanceDto getTeamPerformance(
            @PathVariable Long teamId
    ) {
        return reportService.getTeamPerformance(teamId);
    }
}