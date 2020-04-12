package com.jsrdxzw.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author xuzhiwei
 * @date 2020/04/05
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncTaskConf {

    public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    @Bean("rabbitmq_client_sender")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(CORE_POOL_SIZE * 2);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("rabbitmq_client_async_sender-");
        executor.setRejectedExecutionHandler((r, exec) -> log.error("async sender is rejected, runnable:{},executor:{}", r, exec));
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(180);
        return executor;
    }
}
