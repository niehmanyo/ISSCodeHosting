package com.iss.common.config;

import com.iss.common.factory.LockFactory;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@Slf4j
public class LockConfig {

    //@Value("${spring.redis.host}")
    @Value("${iss.redis.host}")
    private String redisHost;

    //@Value("${spring.redis.port}")
    @Value("${iss.redis.port}")
    private int redisPort;

    @Bean
    @ConditionalOnProperty(name = "iss.redis.lock.enabled", havingValue = "true")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%d", redisHost, redisPort));
        log.info(redisHost);
        return Redisson.create(config);
    }

    @Bean
    @ConditionalOnProperty(name = "iss.redis.lock.enabled", havingValue = "true")
    public LockFactory lockFactory(RedissonClient redissonClient) {
        return new LockFactory(redissonClient);
    }
}
