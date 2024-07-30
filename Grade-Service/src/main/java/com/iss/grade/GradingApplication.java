package com.iss.grade;

import com.iss.api.config.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.iss.api.client", defaultConfiguration = DefaultFeignConfig.class)
@SpringBootApplication
public class GradingApplication {
    public static void main(String[] args) {
        SpringApplication.run(GradingApplication.class, args);
    }
}