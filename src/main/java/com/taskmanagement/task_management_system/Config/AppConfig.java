package com.taskmanagement.task_management_system.Config;

import com.taskmanagement.task_management_system.Config.auditing.AuditorAwareRef;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class AppConfig {

    @Bean
    public AuditorAware<String> auditorAwareRef() {
        return new AuditorAwareRef();
    }
}
