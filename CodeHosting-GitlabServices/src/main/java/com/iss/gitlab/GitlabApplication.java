package com.iss.gitlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.iss"})
public class GitlabApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitlabApplication.class, args);
    }

}
