package com.taskmanagement.task_management_system.Service.notification;

import com.taskmanagement.task_management_system.Base.NotificationSender;
import com.taskmanagement.task_management_system.Enum.NotificationStatus;
import com.taskmanagement.task_management_system.Enum.NotificationType;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationRequest;
import com.taskmanagement.task_management_system.Model.entity.Notification;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.NotificationRepository;
import com.taskmanagement.task_management_system.Repository.UserRepository;
import com.taskmanagement.task_management_system.Service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.notification.UnableToSendNotificationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailNotificationSender implements NotificationSender {

   private final UserRepository userRepository;
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    @Value("${spring.mail.username}")
    private String fromEmail;


    @Override
    public NotificationType getType() {
        return NotificationType.EMAIL;
    }

    @Override
    public void send(NotificationRequest request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        emailService.sendTemplateMessage(
                user.getEmail(),
                user.getFirstName(),
                request.getSubject(),
                request.getContent()
        );

    }
}
