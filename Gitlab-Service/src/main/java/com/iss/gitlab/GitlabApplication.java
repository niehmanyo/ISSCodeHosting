package com.iss.gitlab;

import com.iss.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.iss.api.client", defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("com.iss.gitlab.mapper")
@SpringBootApplication
public class GitlabApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitlabApplication.class, args);
    }
}