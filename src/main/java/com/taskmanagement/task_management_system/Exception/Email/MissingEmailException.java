package com.taskmanagement.task_management_system.Exception.Email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Getter
@Setter
public class MissingEmailException extends OAuth2AuthenticationException {
    private final String providerId;
    private final String provider;
    private final String name;

    public MissingEmailException(String providerId, String provider, String name) {
        super("Email not found...");
        this.providerId = providerId;
        this.provider = provider;
        this.name = name;
    }
}
