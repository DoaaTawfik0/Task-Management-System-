package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class EmailController {
    private final EmailService service;

    @PostMapping("/send")
    public String sendEmail
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @RequestParam Long to_userId,
             @RequestParam String subject,
             @RequestParam String text) {
        service.sendSimpleMessage(customUserDetails.getUsername(), to_userId , subject , text);
        return "Email sent successfully! to " + to_userId + " from " + customUserDetails.getUsername();

    }

    @PostMapping("/template")
    public String sendTemplateEmail
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @RequestParam Long to_userId,
             @RequestParam String name,
             @RequestParam String subject,
             @RequestParam String content
            ) {
       service.sendTemplateMessage(customUserDetails.getUsername(),to_userId , name , subject , content);
        return "Email Template sent successfully!";

    }

}
