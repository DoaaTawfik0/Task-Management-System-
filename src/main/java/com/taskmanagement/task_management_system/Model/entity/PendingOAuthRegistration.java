package com.taskmanagement.task_management_system.Model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingOAuthRegistration {
    @Id
    private String token;

    private String provider;

    private String providerId;

    private String username;

    private String email;

    private String verificationToken;

    private LocalDateTime expiryDate;
}
