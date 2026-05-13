package com.taskmanagement.task_management_system.Model.dto.comment;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentInfo(
        Long id,
        String content,
        String createdBy,
        LocalDateTime createdAt) {
}