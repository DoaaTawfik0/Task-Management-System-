package com.taskmanagement.task_management_system.Security.oauth2;

import com.taskmanagement.task_management_system.Enum.AuthProvider;
import com.taskmanagement.task_management_system.Enum.UserRole;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.UserRepository;
import com.taskmanagement.task_management_system.Utility.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository repository;

    @Override
    public void onAuthenticationSuccess(
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response,
            org.springframework.security.core.Authentication authentication
    ) throws IOException {

        OAuth2AuthenticationToken authToken =
                (OAuth2AuthenticationToken) authentication;

        String registrationId =
                authToken.getAuthorizedClientRegistrationId();

        AuthProvider provider = mapProvider(registrationId);

        OAuth2User oauthUser = authToken.getPrincipal();

        // ✅ SAFE EMAIL HANDLING
        String email = oauthUser.getAttribute("email");

        if (email == null) {
            if ("github".equalsIgnoreCase(registrationId)) {
                email = oauthUser.getAttribute("login") + "@github.com";
            } else {
                email = oauthUser.getAttribute("sub");
            }
        }

        if (email == null) {
            throw new RuntimeException("Email not found from OAuth provider");
        }

        String fullName = resolveName(oauthUser, registrationId);
        String picture = resolvePicture(oauthUser, registrationId);
        String providerId = resolveProviderId(oauthUser, registrationId);

        final String finalEmail = email;

        Users user = repository.findByEmail(email)
                .map(existing -> {
                    existing.setFirstName(extractFirstName(fullName));
                    existing.setLastName(extractLastName(fullName));
                    existing.setProfileImage(picture);
                    existing.setProvider(provider);
                    existing.setProviderId(providerId);
                    return repository.save(existing);
                })
                .orElseGet(() -> {
                    Users newUser = new Users();
                    newUser.setEmail(finalEmail);
                    newUser.setUsername(generateUsername(finalEmail));
                    newUser.setFirstName(extractFirstName(fullName));
                    newUser.setLastName(extractLastName(fullName));
                    newUser.setProfileImage(picture);
                    newUser.setRole(UserRole.USER);
                    newUser.setProvider(provider);
                    newUser.setProviderId(providerId);
                    return repository.save(newUser);
                });

        String token = jwtService.generateToken(user);

        response.sendRedirect(
                "http://localhost:3000/oauth2/success?token=" + token
        );
    }

    // ---------------- HELPERS ----------------

    private AuthProvider mapProvider(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> AuthProvider.GOOGLE;
            case "github" -> AuthProvider.GITHUB;
            default -> throw new RuntimeException("Unsupported provider: " + registrationId);
        };
    }

    private String resolveName(OAuth2User user, String provider) {
        if ("github".equalsIgnoreCase(provider)) {
            return user.getAttribute("name") != null
                    ? user.getAttribute("name")
                    : user.getAttribute("login");
        }
        return user.getAttribute("name");
    }

    private String resolvePicture(OAuth2User user, String provider) {
        if ("github".equalsIgnoreCase(provider)) {
            return user.getAttribute("avatar_url");
        }
        return user.getAttribute("picture");
    }

    private String resolveProviderId(OAuth2User user, String provider) {
        if ("github".equalsIgnoreCase(provider)) {
            return String.valueOf(user.getAttribute("id"));
        }
        return user.getAttribute("sub");
    }

    private String extractFirstName(String fullName) {
        if (fullName == null || fullName.isBlank()) return "User";
        return fullName.split(" ")[0];
    }

    private String extractLastName(String fullName) {
        if (fullName == null || fullName.isBlank()) return "OAuth";
        return fullName.contains(" ")
                ? fullName.substring(fullName.indexOf(" ") + 1)
                : "User";
    }

    private String generateUsername(String email) {
        String base = email.split("@")[0];
        String username = base;
        int counter = 1;

        while (repository.findByUsername(username).isPresent()) {
            username = base + counter;
            counter++;
        }

        return username;
    }
}