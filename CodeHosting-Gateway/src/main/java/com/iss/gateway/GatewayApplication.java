package com.iss.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.iss.gateway",
        "com.iss"
},
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.iss\\.common\\.(?!utils).*"))
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
