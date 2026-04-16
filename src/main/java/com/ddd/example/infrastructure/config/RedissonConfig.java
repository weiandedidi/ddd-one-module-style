package com.ddd.example.infrastructure.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author GuoBin
 * @date 2022/11/29
 * @describe
 */
//@Configuration
public class RedissonConfig {
    //    @Value("${spring.redis.host}")
    private String host;
    //    @Value("${spring.redis.port}")
    private String port;

    //    @Value("${spring.redis.database}")
    private String database;

    //    @Value("${spring.redis.password}")
    private String password;

    @Value("${redisson.coreSize}")
    private String coreSize;
    @Value("${redisson.connectSize}")
    private String connectSize;
    @Value("${redisson.idleSize}")
    private String ideSize;
    @Value("${redisson.timeout}")
    private String timeout;

    /**
     * 依赖k8s的调度，在主机上配置好redis
     *
     * @return
     */
//    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        //线程池优化 默认16 → 降低，降低redis的连接压力
        config.setThreads(8);
        config.setNettyThreads(8);
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password)
                .setDatabase(Integer.parseInt(database))
                .setConnectionPoolSize(Integer.parseInt(connectSize))          // 默认64 → 降低
                .setConnectionMinimumIdleSize(Integer.parseInt(ideSize))   // 默认24 → 降低
                .setTimeout(Integer.parseInt(timeout));
        return Redisson.create(config);
    }


}
