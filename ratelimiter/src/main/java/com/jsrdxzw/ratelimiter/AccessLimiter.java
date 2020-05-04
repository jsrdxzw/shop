package com.jsrdxzw.ratelimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xuzhiwei
 * @date 2020/04/29
 */
@Service
public class AccessLimiter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisScript<Boolean> redisScript;

    public void limitAccess(String key, int limit, TimeUnit timeUnit) {
        Boolean acquired = stringRedisTemplate.execute(
                redisScript,
                List.of(key),
                String.valueOf(limit)
        );
        if (!acquired) {
            throw new RuntimeException("Your access is blocked");
        }
    }
}
