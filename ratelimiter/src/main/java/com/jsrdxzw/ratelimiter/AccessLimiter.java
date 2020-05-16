package com.jsrdxzw.ratelimiter;

import java.lang.annotation.*;

/**
 * @author xuzhiwei
 * @date 2020/05/04
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessLimiter {
    int limit();

    String key() default "";
}
