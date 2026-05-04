package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.dto.notification.NotificationRequest;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse;
import com.taskmanagement.task_management_system.Service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService service;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> get( @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(service.getAll(authHeader));
    }

    @PostMapping
    public ResponseEntity<Void> send(@RequestBody NotificationRequest request) {
        service.sendNotification(request);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> read(@PathVariable Long id) {
        service.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}
