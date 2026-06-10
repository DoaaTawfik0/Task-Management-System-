package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Model.dto.dashboard.DashboardResponse;
import com.taskmanagement.task_management_system.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("dashboard/")
public class DashboardController {
    private final DashboardService service;

    @GetMapping("summary")
    public ResponseEntity<DashboardResponse> summary(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(service.summary(currentUser.user().getId()));
    }
}
