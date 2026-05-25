package com.taskmanagement.task_management_system.Config.auditing;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareRef implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .filter(username -> !"anonymousUser".equals(username))
                .or(() -> Optional.of("SYSTEM")); // default if not logged in
    }
}