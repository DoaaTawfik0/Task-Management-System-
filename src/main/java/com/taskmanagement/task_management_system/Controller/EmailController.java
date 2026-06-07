package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Model.dto.email.EmailRequest;
import com.taskmanagement.task_management_system.Model.dto.email.EmailTemplateRequest;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Service.EmailService;
import com.taskmanagement.task_management_system.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class EmailController {
    private final EmailService service;
    private final UserService userService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @Valid @RequestBody EmailRequest request) {
        Users replyToUser = userService.findByUsername(customUserDetails.getUsername());

        service.sendSimpleMessage(replyToUser.getEmail(), request.getUserId(), request.getSubject(), request.getContent());
        return ResponseEntity.accepted().build();

    }

    @PostMapping("/template")
    public ResponseEntity<String> sendTemplateEmail
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @Valid @RequestBody EmailTemplateRequest request
            ) {
        service.sendTemplateMessage(customUserDetails.getUsername(), request.getUserId(), request.getName(), request.getSubject(), request.getContent());
        return ResponseEntity.accepted().build();

    }

}
