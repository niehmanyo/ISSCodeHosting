package com.iss.common.annotation;

import com.iss.common.enumeration.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String lockName();
    LockType lockType() default LockType.REENTRANT;
}
