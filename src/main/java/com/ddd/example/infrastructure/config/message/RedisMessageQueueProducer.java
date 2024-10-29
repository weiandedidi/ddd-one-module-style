package com.ddd.example.infrastructure.config.message;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消息的生产者，是工具类，消息的消费者是流量的入口，不放在一起, 使用打开注释
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-05 16:58
 */
//@Component
public class RedisMessageQueueProducer {

    //    @Autowired
    RedissonClient redissonClient;

    //    @Value("${message.topic}")
    private String refreshTopic;


    /**
     * 这个只发更新缓存的广播消息
     * RTopic 会发送全client的广播消息。
     */
    public void publishRefreshMessage(String message) {
        RTopic rTopic = redissonClient.getTopic(refreshTopic);
        rTopic.publish(message);
    }
}
