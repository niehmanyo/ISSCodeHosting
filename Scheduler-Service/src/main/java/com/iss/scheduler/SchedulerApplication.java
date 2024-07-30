package com.iss.scheduler;

import com.iss.api.config.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients(basePackages = "com.iss.api.client", defaultConfiguration = DefaultFeignConfig.class)
@SpringBootApplication
@EnableScheduling
public class SchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class);
    }
}