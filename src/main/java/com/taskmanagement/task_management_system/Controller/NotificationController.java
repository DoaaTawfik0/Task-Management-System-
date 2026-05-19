package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.dto.notification.NotificationRequest;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationUnreadResponse;
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

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> get( @RequestHeader("Authorization") String authHeader , @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id , authHeader));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnread( @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(service.getUnread(authHeader));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<NotificationUnreadResponse> count(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(service.countUnread(authHeader));
    }


    @PostMapping
    public ResponseEntity<Void> send(@RequestBody NotificationRequest request) {
        service.sendNotification(request);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> read(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        service.markAsRead(authHeader,id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> readAll(@RequestHeader("Authorization") String authHeader) {
        service.markAllAsRead(authHeader);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("Authorization") String authHeader
            ,@PathVariable Long id) {

        service.delete(id ,authHeader);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestHeader("Authorization") String authHeader) {
        service.deleteAll(authHeader);
        return ResponseEntity.noContent().build();
    }
}
