package com.ddd.example.infrastructure.utils.httpdto;

import lombok.Data;

import java.util.Map;

/**
 * @author maqidi
 * @version 1.0
 * @create 2024-07-09 09:53
 */
@Data
public class JsonStringHttpResponse {

    /**
     * 请求返回的header头部信息
     */
    private Map<String, String> headers;
    /**
     * 返回的消息体
     */
    private String body;

}
