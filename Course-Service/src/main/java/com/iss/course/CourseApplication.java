package com.iss.course;

import com.iss.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.iss.api.client", defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("com.iss.course.mapper")
@SpringBootApplication
public class CourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class);
    }
}