package com.jsrdxzw.ratelimiter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author xuzhiwei
 * @date 2020/05/04
 */
@Slf4j
@Aspect
@Component
public class AccessLimiterAspect {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisScript<Boolean> redisScript;

    @Pointcut("@annotation(com.jsrdxzw.ratelimiter.AccessLimiter)")
    public void cut() {
    }

    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AccessLimiter annotation = method.getAnnotation(AccessLimiter.class);
        if (annotation == null) {
            return;
        }

        String key = annotation.key();
        int limit = annotation.limit();

        Boolean acquired = stringRedisTemplate.execute(
                redisScript,
                List.of(key),
                String.valueOf(limit)
        );
        if (acquired == null || !acquired) {
            log.error("your access is blocked, key={}", key);
            throw new RuntimeException("Your access is blocked");
        }
    }

}
