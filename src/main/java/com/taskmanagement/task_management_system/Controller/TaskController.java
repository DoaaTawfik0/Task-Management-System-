package com.taskmanagement.task_management_system.Controller;


import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping()
    public ResponseEntity<TaskInfo> addTask(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(service.addTask(request));
    }

    @GetMapping
    public ResponseEntity<List<TaskInfo>> getTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long assignedTo
    ) {
        return ResponseEntity.ok(service.getTasks(status, priority, assignedTo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskInfo> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTaskById(id));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<TaskInfo> updateTask(@PathVariable Long id,
                                               @Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(service.updateTaskById(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
        service.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PatchMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<String> assignUser(@PathVariable Long taskId,
                                             @PathVariable Long userId) {

        service.assignUser(taskId, userId);

        return ResponseEntity.ok("User with id:" + userId + " is assigned successfully...");
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<String> assignUsers(@PathVariable Long taskId,
                                              @RequestBody List<Long> userIds) {

        service.assignUsers(taskId, userIds);
        return ResponseEntity.ok("Users assigned successfully...");
    }

    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskInfo> updateTaskStatus(@PathVariable Long id,
                                                     @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(service.updateStatus(id, request.status()));
    }

    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    @PatchMapping("/{id}/priority")
    public ResponseEntity<TaskInfo> updateTaskPriority(@PathVariable Long id,
                                                       @RequestBody UpdatePriorityRequest request) {
        return ResponseEntity.ok(service.updatePriority(id, request.priority()));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{taskId}/unassign/{userId}")
    public ResponseEntity<String> unassignUser(@PathVariable Long taskId,
                                               @PathVariable Long userId) {

        service.unassignUser(taskId, userId);
        return ResponseEntity.ok("User is unassigned successfully...");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/{taskId}/unassign")
    public ResponseEntity<String> unassignUsers(@PathVariable Long taskId,
                                                @RequestBody List<Long> userIds) {

        service.unassignUsers(taskId, userIds);
        return ResponseEntity.ok("Users are unassigned successfully...");
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{taskId}/users")
    public ResponseEntity<Page<UserData>> getAssignedUsers(@PathVariable Long taskId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "username"));

        return ResponseEntity.ok(service.getAssignedUsers(taskId, pageable));
    }

    @PreAuthorize("hasRole('MANAGER')")
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
