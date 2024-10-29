package com.ddd.example.application.consumer;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 消息接收的管理器
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-05 19:15
 */
//@Component
public class MessageListenerHandle {

//    @Value("${message.topic}")
    private String refreshTopic;

    /**
     * 这里和生产者com.ddd.example.infrastructure.config.message.RedisMessageQueueProducer虽然像是因为是自产自销
     * 但是不应该写在一起，消费者是流量入口
     */
//    @Autowired
    RedissonClient redissonClient;
//    @Autowired
    RefreshMessageListener refreshMessageListener;


    /**
     * 监听器实例化以后，注入到监听器中
     */
//    @PostConstruct
    private void subscribeToTopic() {
        RTopic rTopic = redissonClient.getTopic(refreshTopic);
        rTopic.addListener(String.class, refreshMessageListener);
    }


}
