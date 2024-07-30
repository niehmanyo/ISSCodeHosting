package com.iss.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "iss.jwt")
public class JwtProperties {
    private Resource location;
    private String password;
    private String alias;
    private Duration accessTokenTTL = Duration.ofMinutes(30);
    private Duration refreshTokenTTL = Duration.ofMinutes(60);
}
