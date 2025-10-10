package com.driven.dm.global.config;

import com.driven.dm.global.config.security.SecurityAuditorAware;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return new SecurityAuditorAware();
    }
}
