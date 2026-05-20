package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationRequest;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationUnreadResponse;
import com.taskmanagement.task_management_system.Service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService service;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> get(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(service.getAll(currentUser.user().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> get(@AuthenticationPrincipal CustomUserDetails currentUser , @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id , currentUser.user().getId()));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnread(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(service.getUnread(currentUser.user().getId()));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<NotificationUnreadResponse> count(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(service.countUnread(currentUser.user().getId()));
    }


    @PostMapping
    public ResponseEntity<Void> send(@RequestBody NotificationRequest request) {
        service.validateNotificationRequest(request);
        service.sendNotification(request);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> read(@AuthenticationPrincipal CustomUserDetails currentUser, @PathVariable Long id) {
        service.markAsRead(currentUser.user().getId(),id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> readAll(@AuthenticationPrincipal CustomUserDetails currentUser) {
        service.markAllAsRead(currentUser.user().getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails currentUser
            ,@PathVariable Long id) {

        service.delete(id ,currentUser.user().getId());
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal CustomUserDetails currentUser) {
        service.deleteAll(currentUser.user().getId());
        return ResponseEntity.noContent().build();
    }
}
