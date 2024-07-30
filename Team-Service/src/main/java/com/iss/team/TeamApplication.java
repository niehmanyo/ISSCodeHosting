package com.iss.team;

import com.iss.api.config.DefaultFeignConfig;
import com.iss.common.aspect.LockAspect;
import com.iss.common.config.LockConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("com.iss.team.mapper")
@EnableFeignClients(basePackages = "com.iss.api.client", defaultConfiguration = DefaultFeignConfig.class)
@Import({LockConfig.class, LockAspect.class})
public class TeamApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamApplication.class, args);
    }

}
