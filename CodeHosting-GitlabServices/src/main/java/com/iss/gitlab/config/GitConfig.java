package com.iss.gitlab.config;

import org.gitlab4j.api.GitLabApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitConfig {

    @Value("http://43.156.15.105/")
    private String gitLabHostUrl;

    @Value("glpat-g7jH3EhVs1snv_werxaG")
    private String accessToken;

    public static String host = "http://43.156.15.105/";

    public static String access_token = "glpat-g7jH3EhVs1snv_werxaG";

    @Bean
    public GitLabApi getGitLabApi(){
        return new GitLabApi(gitLabHostUrl,accessToken);
    }
}
