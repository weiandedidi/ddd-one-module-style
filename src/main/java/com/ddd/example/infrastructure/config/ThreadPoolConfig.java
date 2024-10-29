package com.ddd.example.infrastructure.config;

import com.ddd.example.infrastructure.utils.TraceThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

/**
 * 配置一个带有traceId的线程池，底层是自定义线程池大小
 * 线程池的使用，使用traceThreadPool即可
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-01 10:17
 */
@Configuration
public class ThreadPoolConfig {

    @Value("${customer.corePoolSize}")
    private int corePoolSize;

    @Value("${customer.maximumPoolSize}")
    private int maximumPoolSize;

    @Value("${customer.keepAliveTime}")
    private long keepAliveTime;

    @Value("${customer.queueCapacity}")
    private int queueCapacity;


    @Bean
    public ExecutorService traceThreadPool() {
        // 创建一个固定大小的线程池，cpu的2倍
        return TraceThreadPool.newCustomThreadPool(corePoolSize, maximumPoolSize, keepAliveTime, queueCapacity);
    }
}
