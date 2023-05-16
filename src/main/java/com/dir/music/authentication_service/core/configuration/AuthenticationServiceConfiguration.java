package com.dir.music.authentication_service.core.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "authentication-service")
@PropertySource(value = "classpath:application.yml")
@Data
public class AuthenticationServiceConfiguration {
    private String jwtAccessTokenSecret;
    private String jwtRefreshTokenSecret;
    private String jwtIssuer;
    private String jwtAudience;
    private int passwordHashStrength;
}
