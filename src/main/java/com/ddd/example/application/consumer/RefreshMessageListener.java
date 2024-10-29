package com.ddd.example.application.consumer;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * 刷新消息的监听类
 * 刷新索引、刷新本地缓存
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-05 18:57
 */
//@Component
@Slf4j
public class RefreshMessageListener implements MessageListener<String> {

    /**
     * @param channel 消息的频道，topic
     * @param message 内容
     */
    @Override
    public void onMessage(CharSequence channel, String message) {
        //监听到刷新词表的信息,处理一个service，刷新ram的词表
        log.info("RefreshMessageListener onMessage channel:{}, message:{}", channel, message);
    }


}
