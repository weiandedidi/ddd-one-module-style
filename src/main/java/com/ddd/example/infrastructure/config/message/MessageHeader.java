package com.ddd.example.infrastructure.config.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 消息头设置
 * 这个类不要设置默认值，可以作为接受类使用（设置默认值，就得重写一个类防腐，本来是接受类就应该防腐）
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-05 17:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageHeader {
    private String messageId;
    private String messageType;
    private Date timestamp;
    /**
     * 发送消息的时候直接设置，接收的时候也就设置了，适用于自产自销的服务（其他服务的消息，需要特殊处理）
     */
    private String traceId;
    /**
     * 消息发出的服务
     */
    private String source;
    /**
     * 消息接收的服务
     */
    private String destination;
}
