package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Exception.Email.EmailSendingException;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public String getRecipient(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user not found with id: " + userId)
        );
        return user.getEmail();

    }

    @Async
    public void sendSimpleMessage(String replyToEmail, Long userId, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setReplyTo(replyToEmail);
        message.setTo(getRecipient(userId));
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);

    }
    @Async

    public void sendTemplateMessage(String replyTo, Long userId, String name, String subject, String content) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("content", content);


        String processHtml = templateEngine.process("email-template", context);
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, "utf-8");

        try {
            helper.setText(processHtml, true);
            helper.setReplyTo(replyTo);
            helper.setTo(getRecipient(userId));
            helper.setSubject(subject);
            helper.setFrom(fromEmail);
            mailSender.send(mimeMailMessage);
        } catch (MessagingException | MailException e) {
            throw new EmailSendingException("Failed to send email");
        }
    }

    @Async
    public void sendEmail(String email, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setReplyTo(null);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Async
    public void sendTemplateEmail(String email ,
                                  String templateName,
                                  Context context) {


        String processHtml = templateEngine.process(templateName, context);


        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setText(processHtml, true);

            mailSender.send(message);
        }catch (MessagingException | MailException e) {
            throw new EmailSendingException("Failed to send email");

        }

    }
    public boolean verifyMailExist(String email) {
        return userRepository.existsByEmail(email);
    }
}
