package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationUnreadResponse;
import com.taskmanagement.task_management_system.Model.entity.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends BaseRepository<Notification, Long> {

    @Query("""
            SELECT new com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse
            (
                n.content,
                n.subject,
                n.notificationStatus
            )
            FROM Notification n
            WHERE n.user.id = :id
            """)
    List<NotificationResponse> findAllByUserId(@Param("id") Long id);


    @Query("""
            SELECT new com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse
            (
                n.content,
                n.subject,
                n.notificationStatus
            )
            FROM Notification n
            WHERE n.user.id = :id
            AND n.notificationStatus =
                com.taskmanagement.task_management_system.Enum.NotificationStatus.UNREAD
            """)
    List<NotificationResponse> findAllUnreadByUserId(@Param("id") Long id);


    @Query("""
            SELECT new com.taskmanagement.task_management_system.Model.dto.notification.NotificationUnreadResponse
            (
                COUNT(n.id)
            )
            FROM Notification n
            WHERE n.user.id = :id
            AND n.notificationStatus =
                com.taskmanagement.task_management_system.Enum.NotificationStatus.UNREAD
            """)
    NotificationUnreadResponse countUnreadByUserId(@Param("id") Long id);

}