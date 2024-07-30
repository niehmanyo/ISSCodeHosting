package com.iss.gitlab.config;

import org.gitlab4j.api.GitLabApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitlabConfig {

    @Value("${iss.gitlab.host-url}")
    private String gitlabHostUrl;

    @Value("${iss.gitlab.personal-access-token}")
    private String personalAccessToken;

    @Bean
    public GitLabApi gitLabApi() {
        return new GitLabApi(gitlabHostUrl, personalAccessToken);
    }
}
