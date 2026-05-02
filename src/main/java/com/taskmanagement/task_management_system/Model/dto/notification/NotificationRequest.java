package com.taskmanagement.task_management_system.Model.dto.notification;

import com.taskmanagement.task_management_system.Enum.NotificationStatus;
import com.taskmanagement.task_management_system.Enum.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String content;

    private String recipient;

    private String subject;

    private NotificationType notificationType;

    private NotificationStatus notificationStatus;
}
