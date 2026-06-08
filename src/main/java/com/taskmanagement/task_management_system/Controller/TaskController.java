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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

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

    @PutMapping("/{id}")
    public ResponseEntity<TaskInfo> updateTask(@PathVariable Long id,
                                               @Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(service.updateTaskById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
        service.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<String> assignUser(@PathVariable Long taskId,
                                             @PathVariable Long userId) {

        service.assignUser(taskId, userId);

        return ResponseEntity.ok("User with id:" + userId + " is assigned successfully...");
    }

    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<String> assignUsers(@PathVariable Long taskId,
                                              @RequestBody List<Long> userIds) {

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

    @PatchMapping("/{taskId}/unassign/{userId}")
    public ResponseEntity<String> unassignUser(@PathVariable Long taskId,
                                               @PathVariable Long userId) {

        service.unassignUser(taskId, userId);
        return ResponseEntity.ok("User is unassigned successfully...");
    }

    @PatchMapping("/{taskId}/unassign")
    public ResponseEntity<String> unassignUsers(@PathVariable Long taskId,
                                                @RequestBody List<Long> userIds) {

        service.unassignUsers(taskId, userIds);
        return ResponseEntity.ok("Users are unassigned successfully...");
    }

    @GetMapping("/{taskId}/users")
    public ResponseEntity<Page<UserData>> getAssignedUsers(@PathVariable Long taskId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {

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


}
