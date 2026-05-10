package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.dto.CommentInfo;
import com.taskmanagement.task_management_system.Model.dto.comment.CommentRequest;
import com.taskmanagement.task_management_system.Model.entity.Task;
import com.taskmanagement.task_management_system.Service.CommentService;
import com.taskmanagement.task_management_system.Service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final TaskService taskService;

    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<CommentInfo> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequest request
    ) {

        Task task = taskService.getTaskEntity(taskId);

        return ResponseEntity.ok(
                commentService.addComment(task, request)
        );
    }

    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<List<CommentInfo>> getTaskComments(
            @PathVariable Long taskId
    ) {

        Task task = taskService.getTaskEntity(taskId);

        return ResponseEntity.ok(
                commentService.getTaskComments(task)
        );
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentInfo> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request
    ) {

        return ResponseEntity.ok(
                commentService.updateComment(id, request)
        );
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id
    ) {

        commentService.deleteComment(id);

        return ResponseEntity.noContent().build();
    }
}