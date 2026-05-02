package com.taskmanagement.task_management_system.Service.notification;

import com.taskmanagement.task_management_system.Base.NotificationSender;
import com.taskmanagement.task_management_system.Enum.NotificationType;
import com.taskmanagement.task_management_system.Model.entity.Notification;
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

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String fromEmail;


    @Override
    public NotificationType getType() {
        return NotificationType.EMAIL;
    }

    @Override
    public void send(Notification notification) throws UnableToSendNotificationException {

        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, "utf-8");
        try {
            helper.setText(notification.getContent(), true);
            helper.setTo(notification.getRecipient());
            helper.setSubject(notification.getSubject());
            helper.setFrom(fromEmail);
            mailSender.send(mimeMailMessage);
        } catch (Exception e) {
            throw new UnableToSendNotificationException("Failed to send email", e);
        }

    }
}
