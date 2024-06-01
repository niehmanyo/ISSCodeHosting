package com.iss.common.config;

import org.gitlab4j.api.GitLabApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitlabConfig {

    @Value("")
    private String gitLabHostUrl;

    @Value("")
    private String accessToken;

    @Bean
    public GitLabApi gitLabApi(){
        return new GitLabApi(gitLabHostUrl,accessToken);
    }
}
