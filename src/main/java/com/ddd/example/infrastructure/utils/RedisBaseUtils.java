package com.ddd.example.infrastructure.utils;

import com.ddd.example.infrastructure.valueobject.RedisUseCaseEnum;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 这是redis的基础操作类，底层基于redisson，之后要使用.
 * 使用的时候需要打开注解
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-05 16:46
 */
//@Component
public class RedisBaseUtils {

    //消息的前缀， 生成key的方法
    private static final String PREFIX = "demo";
    //分割符|
    private static final String SPLIT = "|";

    //    @Autowired
    private RedissonClient redissonClient;

    /**
     * 生成key的方法目前，前缀更衣封装
     *
     * @param key
     * @param useCaseEnum redis使用场景
     */
    public static String generateKey(String key, RedisUseCaseEnum useCaseEnum) {
        return PREFIX + SPLIT + useCaseEnum.getValue() + SPLIT + key;
    }


    public void setString(String key, String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }


    /**
     * 有失效时间的键值对
     *
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     */
    public void setStringWithExpiredTime(String key, String value, long timeout, TimeUnit timeUnit) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value, timeout, timeUnit);
    }

    /**
     * 获取键值对
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 删除键值对
     *
     * @param key
     */
    public boolean deleteString(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (!bucket.isExists()) {
            return true;
        }
        return bucket.delete();
    }

    public void setHashValue(String key, String hashKey, String value) {
        RMap<String, String> map = redissonClient.getMap(key);
        map.put(hashKey, value);
    }

    public void setHashMapObject(String key, Map<String, String> map) {
        RMap<String, String> redisMap = redissonClient.getMap(key);
        redisMap.putAll(map);
    }

    public String getHash(String key, String hashKey) {
        RMap<String, String> map = redissonClient.getMap(key);
        return map.get(hashKey);
    }

    public Map<String, String> getHashMap(String key) {
        RMap<String, String> map = redissonClient.getMap(key);
        return map.readAllMap();
    }

    public Object deleteHash(String key, String hashKey) {
        RMap<String, Object> map = redissonClient.getMap(key);
        if (!map.isExists()) {
            return null;
        }
        return map.remove(hashKey);
    }

    /**
     * 删除hashMap所有
     *
     * @param key
     * @return
     */
    public Object deleteHashMap(String key) {
        RMap<String, Object> map = redissonClient.getMap(key);
        if (!map.isExists()) {
            return null;
        }
        return map.delete();
    }

    /**
     * 设置boolean值
     *
     * @param key
     * @param flag
     */
    public void setBoolean(String key, boolean flag) {
        RBucket<Boolean> bucket = redissonClient.getBucket(key);
        bucket.set(flag);
    }

    /**
     * 获取boolean
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(String key) {
        RBucket<Boolean> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 删除开关
     *
     * @param key
     */
    public boolean deleteBoolean(String key) {
        RBucket<Boolean> bucket = redissonClient.getBucket(key);
        if (!bucket.isExists()) {
            return true;
        }
        return bucket.delete();
    }

}
