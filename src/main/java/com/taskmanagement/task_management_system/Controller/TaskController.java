package com.taskmanagement.task_management_system.Controller;


import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import com.taskmanagement.task_management_system.Enum.UserRole;
import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Model.dto.task.*;
import com.taskmanagement.task_management_system.Model.dto.user.UserData;
import com.taskmanagement.task_management_system.Service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @PreAuthorize("hasAnyRole('MANAGER','USER')")
    @PostMapping()
    public ResponseEntity<TaskInfo> addTask(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(service.addTask(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public ResponseEntity<List<TaskInfo>> getTasks(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long assignedTo
    ) {
        List<TaskInfo> tasks;
        UserRole role = currentUser.user().getRole();

        if (role == UserRole.ADMIN) {
            tasks = service.getAllTasks(status, priority, assignedTo);
        } else {
            tasks = service.getTasksCreatedBy(currentUser.getUsername(), status, priority, assignedTo);
        }
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskInfo> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTaskById(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<TaskInfo> updateTask(@PathVariable Long id,
                                               @Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(service.updateTaskById(id, request));
    }

    @PreAuthorize("hasAnyRole('MANAGER','USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskById(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {

        service.verifyOwnerOrThrow(service.getTaskEntity(id), currentUser.getUsername());
        service.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<String> assignUser(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long taskId,
            @PathVariable Long userId) {

        service.verifyOwnerOrThrow(service.getTaskEntity(taskId), currentUser.getUsername());

        service.assignUser(taskId, userId);

        return ResponseEntity.ok("User with id:" + userId + " is assigned successfully...");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<String> assignUsers(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long taskId,
            @RequestBody List<Long> userIds) {

        service.verifyOwnerOrThrow(service.getTaskEntity(taskId), currentUser.getUsername());

        service.assignUsers(taskId, userIds);
        return ResponseEntity.ok("Users assigned successfully...");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskInfo> updateTaskStatus(@PathVariable Long id,
                                                     @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(service.updateStatus(id, request.status()));
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<TaskInfo> updateTaskPriority(@PathVariable Long id,
                                                       @RequestBody UpdatePriorityRequest request) {
        return ResponseEntity.ok(service.updatePriority(id, request.priority()));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{taskId}/unassign/{userId}")
    public ResponseEntity<String> unassignUser(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long taskId,
            @PathVariable Long userId) {

        service.verifyOwnerOrThrow(service.getTaskEntity(taskId), currentUser.getUsername());

        service.unassignUser(taskId, userId);
        return ResponseEntity.ok("User is unassigned successfully...");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{taskId}/unassign")
    public ResponseEntity<String> unassignUsers(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long taskId,
            @RequestBody List<Long> userIds) {

        service.verifyOwnerOrThrow(service.getTaskEntity(taskId), currentUser.getUsername());

        service.unassignUsers(taskId, userIds);
        return ResponseEntity.ok("Users are unassigned successfully...");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{taskId}/users")
    public ResponseEntity<Page<UserData>> getAssignedUsers(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        service.verifyOwnerOrThrow(service.getTaskEntity(taskId), currentUser.getUsername());

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "username"));

        return ResponseEntity.ok(service.getAssignedUsers(taskId, pageable));
    }

    @PutMapping("/{taskId}/team/{teamId}")
    public ResponseEntity<TaskInfo> assignTaskToTeam(
            @PathVariable Long taskId,
            @PathVariable Long teamId) {

        return ResponseEntity.ok(
                service.assignTaskToTeam(taskId, teamId)
        );
    }

    @PostMapping("/{taskId}/send-reminder/{userId}")
    public ResponseEntity<String> sendReminder(@PathVariable("taskId") Long taskId, @PathVariable("userId") Long userId) {

        service.sendReminder(taskId, userId);
        return ResponseEntity.ok("Reminder for the task with id: " + taskId + " sent successfully to user with id: " + userId);
    }

    // for testing
    @PostMapping("scheduling-reminder")
    public ResponseEntity<String> SchedulingReminder() {
        service.sendScheduledReminder();
        return ResponseEntity.ok("Reminder sent successfully!");
    }


}
