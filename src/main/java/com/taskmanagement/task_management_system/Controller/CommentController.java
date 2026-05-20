package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.dto.comment.CommentInfo;
import com.taskmanagement.task_management_system.Model.dto.comment.CommentRequest;
import com.taskmanagement.task_management_system.Model.entity.Task;
import com.taskmanagement.task_management_system.Service.CommentService;
import com.taskmanagement.task_management_system.Service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<CommentInfo>> getTaskComments(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        return ResponseEntity.ok(
                commentService.getTaskComments(taskId, pageable)
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