package com.ddd.example.infrastructure.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 内存配置类
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-16 10:33
 */
@Configuration
public class DictFileLoadConfig {
    /**
     * 白词表
     *
     * @return
     */
    @Bean
    public Cache<String, Integer> whiteDictCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(20000)
                .build();
    }

    /**
     * 黑词表
     *
     * @return
     */
    @Bean
    public Cache<String, Integer> blackDictCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(45000)
                .build();
    }
}
