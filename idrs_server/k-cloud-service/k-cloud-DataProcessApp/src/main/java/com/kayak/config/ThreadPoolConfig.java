package com.kayak.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {
    @Value("${kcloud.batch.threadpool.slice.core_pool_size}")
    private Integer corePoolSize;
    @Value("${kcloud.batch.threadpool.slice.max_pool_size}")
    private  Integer maxPoolSize;
    @Value("${kcloud.batch.threadpool.slice.keep_alive_seconds}")
    private Integer keepAliveTime;
    @Bean(name = "threadPool")
    public ThreadPoolTaskExecutor threadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setThreadNamePrefix("default_");
        executor.initialize();
        return executor;
    }
}