package com.iss.common.config;

import org.gitlab4j.api.GitLabApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitlabConfig {

    @Value("http://43.156.15.105/")
    private String gitLabHostUrl;

    @Value("glpat-g7jH3EhVs1snv_werxaG")
    private String accessToken;

    @Bean
    public GitLabApi getGitLabApi(){
        return new GitLabApi(gitLabHostUrl,accessToken);
    }
}
