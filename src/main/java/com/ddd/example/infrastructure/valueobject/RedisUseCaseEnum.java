package com.ddd.example.infrastructure.valueobject;

/**
 * 这个防止底层的infrastructure层的枚举类，例如redis的使用场景，用于作为key使用。
 * 或者以后得其他的底层组件区分使用
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-08 14:06
 */
public enum RedisUseCaseEnum {
    CONFIG("config", "配置"),
    CACHE("cache", "缓存"),
    DISTRIBUTED_LOCK("distributed_lock", "分布式锁"),
    DISTRIBUTED_QUEUE("distributed_queue", "分布式队列"),
    STATISTICS("statistics", "统计");
    /**
     * 内容名字
     */
    private String value;
    /**
     * 说明
     */
    private String desc;

    RedisUseCaseEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
