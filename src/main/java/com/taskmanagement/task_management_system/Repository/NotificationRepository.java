package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse;
import com.taskmanagement.task_management_system.Model.entity.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends BaseRepository<Notification , Long> {
    @Query(
            """
            SELECT new com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse
            (
            n.content,
            n.subject,
            n.notificationStatus
            )
            FROM Notification n
            WHERE  n.user.id = :id
            """
    )
    List<NotificationResponse> findAllByUserId(Long id);
}
