package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Model.entity.PendingOAuthRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingOAuthRegistrationRepository
        extends JpaRepository<PendingOAuthRegistration, String> {

    Optional<PendingOAuthRegistration>
    findByVerificationToken(String verificationToken);
}