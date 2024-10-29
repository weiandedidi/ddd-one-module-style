package com.ddd.example.infrastructure.config.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author maqidi
 * @version 1.0
 * @create 2024-07-05 17:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message<T> {

    private MessageHeader header;
    /**
     * 消息内容
     */
    private T body;
}
