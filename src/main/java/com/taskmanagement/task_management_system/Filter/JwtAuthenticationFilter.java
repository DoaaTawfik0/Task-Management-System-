package com.taskmanagement.task_management_system.Filter;


import com.taskmanagement.task_management_system.Exception.Token.ExpiredTokenException;
import com.taskmanagement.task_management_system.Exception.Token.InvalidTokenException;
import com.taskmanagement.task_management_system.Service.CustomUserDetailsService;
import com.taskmanagement.task_management_system.Utility.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = jwtService.extractToken(header);

        try {
            String email = jwtService.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = userService.loadUserByUsername(email);

                if (jwtService.validateToken(token, email, userDetails)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (ExpiredTokenException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            handleException(response, "Token expired"
                    , "Please login again");
            return;
        } catch (InvalidTokenException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            handleException(response, "Invalid token"
                    , "Authentication failed");
            return;
        } catch (UsernameNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            handleException(response, "Authentication error"
                    , ex.getMessage());
            return;
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            handleException(response, "Authentication error"
                    , "Invalid token");
            return;
        }

        chain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, String error, String message)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format(
                "{\"timeStamp\": \"%s\", \"message\": \"%s\", \"details\": \"%s\"}",
                java.time.LocalDateTime.now(),
                message,
                error
        );

        response.getWriter().write(jsonResponse);
    }
}