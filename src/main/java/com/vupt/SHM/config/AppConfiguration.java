package com.vupt.SHM.config;

import com.vupt.SHM.audit.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;


@Configuration
public class AppConfiguration {
    @Bean
    public AuditorAware<String> auditorAware() {
        return (AuditorAware<String>) new AuditorAwareImpl();
    }
}

