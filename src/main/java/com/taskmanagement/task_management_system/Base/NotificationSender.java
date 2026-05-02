package com.taskmanagement.task_management_system.Base;

import com.taskmanagement.task_management_system.Enum.NotificationType;
import com.taskmanagement.task_management_system.Model.entity.Notification;
import org.springframework.jmx.export.notification.UnableToSendNotificationException;

public interface NotificationSender {

    NotificationType getType();
    void send (Notification notification) throws UnableToSendNotificationException;
}
