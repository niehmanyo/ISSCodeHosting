package com.iss.cicd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.iss"})
public class CicdApplication {
    public static void main(String[] args) {
        SpringApplication.run(CicdApplication.class,args);
    }
}
