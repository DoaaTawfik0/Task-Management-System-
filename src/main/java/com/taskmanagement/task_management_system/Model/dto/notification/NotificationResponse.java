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
public class NotificationResponse {

    private String content;

    private String subject;

    private NotificationStatus notificationStatus;
}
