package com.taskmanagement.task_management_system.Model.entity;

import com.taskmanagement.task_management_system.Base.BaseEntity;
import com.taskmanagement.task_management_system.Enum.NotificationStatus;
import com.taskmanagement.task_management_system.Enum.NotificationType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseEntity<Long> {

    private String content;


    private String subject;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    @ManyToOne
    private Users user;
}
