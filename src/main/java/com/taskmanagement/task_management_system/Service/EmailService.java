package com.taskmanagement.task_management_system.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendSimpleMessage(String relyTo, String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setReplyTo(relyTo);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);

    }

    public void sendTemplateMessage(String to, String name , String subject , String content) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("content", content);


        String processHtml = templateEngine.process("email-template", context);
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, "utf-8");

        try {
            helper.setText(processHtml, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(fromEmail);
            mailSender.send(mimeMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
