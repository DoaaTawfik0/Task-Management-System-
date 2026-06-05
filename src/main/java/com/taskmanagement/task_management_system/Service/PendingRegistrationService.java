package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Model.entity.PendingOAuthRegistration;
import com.taskmanagement.task_management_system.Repository.PendingOAuthRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PendingRegistrationService {

    private final PendingOAuthRegistrationRepository repository;

    private final EmailService emailService;

    public String createPendingRegistration(
            String provider,
            String providerId,
            String username,
            String firstName,
            String lastName

    ) {

        String token = UUID.randomUUID().toString();

        PendingOAuthRegistration registration =
                PendingOAuthRegistration.builder()
                        .token(token)
                        .provider(provider)
                        .providerId(providerId)
                        .username(username)
                        .firstName(firstName)
                        .lastName(lastName)
                        .expiryDate(
                                LocalDateTime.now()
                                        .plusMinutes(30)
                        )
                        .build();

        repository.save(registration);

        return token;
    }


    public void sendVerificationEmail(
            String registrationToken,
            String email
    ) {

        PendingOAuthRegistration registration =
                repository.findById(registrationToken)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Registration not found"
                                )
                        );

        String verificationToken =
                UUID.randomUUID().toString();

        registration.setEmail(email);
        registration.setVerificationToken(
                verificationToken
        );

        repository.save(registration);

        emailService.sendEmail(
                email,
                "Verify Email",
                "Click here: " +
                        "http://localhost:8080/auth/verify-email?token="
                        + verificationToken
        );
    }


    public PendingOAuthRegistration verifyEmail(
            String verificationToken
    ) {

        return repository.findByVerificationToken(
                        verificationToken
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Invalid token"
                        )
                );
    }

    public void delete(PendingOAuthRegistration registration) {
        repository.delete(registration);
    }
}