package com.iss.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "oauth2.client.registration.github")
public class GitHubProperties {
    private String userInfoUri;
    private String tokenUri;
    private String clientId;
    private String clientSecret;
}
