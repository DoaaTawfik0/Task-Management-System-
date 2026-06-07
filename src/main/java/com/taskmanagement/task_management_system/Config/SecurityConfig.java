package com.taskmanagement.task_management_system.Config;

import com.taskmanagement.task_management_system.Filter.JwtAuthenticationFilter;
import com.taskmanagement.task_management_system.Security.oauth2.OAuth2AuthenticationFailureHandler;
import com.taskmanagement.task_management_system.Security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.taskmanagement.task_management_system.Service.CustomOAuth2UserService;
import com.taskmanagement.task_management_system.Service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;
    private final CustomUserDetailsService service;
    private final CustomOAuth2UserService oauth2UserService;
    private final OAuth2AuthenticationSuccessHandler successHandler;
    private final OAuth2AuthenticationFailureHandler failureHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                        "/auth/**",
                                        "/oauth2/**",
                                        "/login/**",
                                        "/login/oauth2/**",
                                        "/v3/api-docs/**",
                                        "/docs/**").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .oauth2Login(oauth ->
                        oauth.authorizationEndpoint(
                                        auth ->
                                                auth.baseUri("/oauth2/authorize")
                                ).redirectionEndpoint(redir ->
                                        redir.baseUri("/login/oauth2/code/*")
                                ).userInfoEndpoint(userInfo ->
                                        userInfo.userService(oauth2UserService)
                                ).successHandler(successHandler)
                                .failureHandler(failureHandler)
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager manager() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider(service);
        p.setPasswordEncoder(encoder());
        return new ProviderManager(p);
    }
}