package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Service.EmailService;
import lombok.RequiredArgsConstructor;
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
            (@RequestParam String to,
             @RequestParam String subject,
             @RequestParam String text) {
        service.sendSimpleMessage(to , subject , text);
        return "Email sent successfully!";

    }

    @PostMapping("/template")
    public String sendTemplateEmail
            (@RequestParam String to,
             @RequestParam String name,
             @RequestParam String subject,
             @RequestParam String content
            ) {
       service.sendTemplateMessage(to , name , subject , content);
        return "Email Template sent successfully!";

    }

}
