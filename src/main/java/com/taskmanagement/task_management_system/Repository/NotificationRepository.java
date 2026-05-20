package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationUnreadResponse;
import com.taskmanagement.task_management_system.Model.entity.Notification;
import org.springframework.data.jpa.repository.Modifying;
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
            WHERE n.id = :id
            """)

    NotificationResponse findNotificationById(@Param("id") Long id);
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
            WHERE n.user.id = :userId AND n.id = :id
            """)
    NotificationResponse findByIdUserId(@Param("userId") Long userId ,@Param("id") Long id);
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

    boolean existsByUserIdAndId(Long userId , Long id);
    @Modifying
    @Query("""
            UPDATE Notification n
          
                SET n.notificationStatus =
                com.taskmanagement.task_management_system.Enum.NotificationStatus.READ
           
            WHERE n.user.id = :userId AND n.id = :id
            """)
    int markAsRead(
            @Param("userId") Long userId,
            @Param("id") Long id
    );
    @Modifying
    @Query("""
      UPDATE Notification n
      
                SET n.notificationStatus =
                com.taskmanagement.task_management_system.Enum.NotificationStatus.READ
      
            WHERE n.user.id = :userId AND n.notificationStatus =
                com.taskmanagement.task_management_system.Enum.NotificationStatus.UNREAD
      """)
    int markAllAsRead(
            @Param("userId") Long userId
    );

    @Modifying
    @Query("""
        DELETE FROM Notification n
        WHERE n.id = :id AND n.user.id = :userId
    """)
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Modifying
    @Query("""
        DELETE FROM Notification n
        WHERE n.user.id = :userId
    """)
    int deleteAllByUserId(@Param("userId") Long userId);
}