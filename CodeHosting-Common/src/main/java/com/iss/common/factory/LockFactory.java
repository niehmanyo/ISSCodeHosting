package com.iss.common.factory;

import com.iss.common.enumeration.LockType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

public class LockFactory {

    private final RedissonClient redissonClient;

    public LockFactory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public Lock createLock(LockType lockType, String lockName) {
        switch (lockType) {
            case REENTRANT:
                return redissonClient.getLock(lockName);
            case FAIR:
                return redissonClient.getFairLock(lockName);
            case SPIN:
                return redissonClient.getSpinLock(lockName);
            default:
                throw new IllegalArgumentException("Unsupported lock type");
        }
    }
}