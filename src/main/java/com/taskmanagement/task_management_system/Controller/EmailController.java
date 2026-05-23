package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Model.dto.email.EmailRequest;
import com.taskmanagement.task_management_system.Model.dto.email.EmailTemplateRequest;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationRequest;
import com.taskmanagement.task_management_system.Service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class EmailController {
    private final EmailService service;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @Valid @RequestBody EmailRequest request) {
        service.sendSimpleMessage(customUserDetails.getUsername(), request.getUserId() , request.getSubject(),request.getContent());
        return ResponseEntity.accepted().build();

    }

    @PostMapping("/template")
    public ResponseEntity<String> sendTemplateEmail
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @Valid @RequestBody EmailTemplateRequest request
            ) {
       service.sendTemplateMessage(customUserDetails.getUsername(), request.getUserId() ,request.getName(), request.getSubject(),request.getContent());
        return ResponseEntity.accepted().build();

    }

}
