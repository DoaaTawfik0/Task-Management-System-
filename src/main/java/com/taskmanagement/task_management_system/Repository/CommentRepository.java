package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Model.dto.comment.CommentInfo;
import com.taskmanagement.task_management_system.Model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends BaseRepository<Comment, Long> {

    @Query("""
            SELECT new com.taskmanagement.task_management_system.Model.dto.comment.CommentInfo(
                c.id,
                c.content,
                c.user.username,
                c.createdAt
            )
            FROM Comment c
            WHERE c.task.id = :taskId
            ORDER BY c.createdAt DESC
            """)
    Page<CommentInfo> findTaskComments(
            @Param("taskId") Long taskId,
            Pageable pageable
    );
}
