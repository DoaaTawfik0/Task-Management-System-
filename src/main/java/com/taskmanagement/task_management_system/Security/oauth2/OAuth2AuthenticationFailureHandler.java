package com.taskmanagement.task_management_system.Security.oauth2;

import com.taskmanagement.task_management_system.Exception.Email.MissingEmailException;
import com.taskmanagement.task_management_system.Service.PendingRegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler
        implements AuthenticationFailureHandler {

    private final PendingRegistrationService pendingService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof MissingEmailException ex) {
            String token =
                    pendingService
                            .createPendingRegistration(
                                    ex.getProvider(),
                                    ex.getProviderId(),
                                    ex.getName(),
                                    ex.getFirstName(),
                                    ex.getLastName()
                            );

            response.sendRedirect(
                    "http://localhost:3000/complete-registration?token="
                            + token
            );

            return;
        }
        response.sendRedirect(
                "/login?error"
        );

    }
}
