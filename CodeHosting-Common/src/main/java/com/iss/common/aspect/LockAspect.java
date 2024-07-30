package com.iss.common.aspect;

import com.iss.common.annotation.DistributedLock;
import com.iss.common.factory.LockFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

@Aspect
@Component
@Slf4j
public class LockAspect {

    @Autowired
    private LockFactory lockFactory;

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        log.info("锁生效了");
        log.info("方法签名: " + joinPoint.getSignature());
        log.info("目标类: " + joinPoint.getTarget().getClass().getName());
        log.info("锁名: " + distributedLock.lockName());
        log.info("锁类型: " + distributedLock.lockType());
        Lock lock = lockFactory.createLock(distributedLock.lockType(), distributedLock.lockName());
        try {
            lock.lock();
            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }
}
