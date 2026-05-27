package com.ddd.example.infrastructure.utils;

import com.ddd.example.infrastructure.valueobject.RedisUseCaseEnum;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
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

    @Resource
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


    // ======================== 以下是追加的 ZSet 4 个方法（带自动续期） ========================

    /**
     * ZSet 添加元素（带分数）+ 自动续期
     *
     * @param key      zset key
     * @param value    元素值
     * @param score    分数
     * @param timeout  续期时间
     * @param timeUnit 时间单位
     * @return true:添加成功 false:已存在
     */
    public boolean zSetAdd(String key, String value, double score, long timeout, TimeUnit timeUnit) {
        RScoredSortedSet<String> zSet = redissonClient.getScoredSortedSet(key);
        boolean result = zSet.add(score, value);
        // 替换废弃方法 → 最新标准续期方式
        zSet.expire(Duration.of(timeout, timeUnit.toChronoUnit()));
        return result;
    }

    /**
     * ZSet 分数自增（不存在则自动创建）+ 自动续期
     *
     * @param key       zset key
     * @param value     元素
     * @param increment 增加的分数
     * @param timeout   续期时间
     * @param timeUnit  时间单位
     * @return 增加后的分数
     */
    public double zSetIncrementScore(String key, String value, double increment, long timeout, TimeUnit timeUnit) {
        RScoredSortedSet<String> zSet = redissonClient.getScoredSortedSet(key);
        double newScore = zSet.addScore(value, increment);
        // 替换废弃方法
        zSet.expire(Duration.of(timeout, timeUnit.toChronoUnit()));
        return newScore;
    }

    /**
     * ZSet 删除指定元素
     *
     * @param key   zset key
     * @param value 要删除的元素
     * @return 删除的数量
     */
    public boolean zSetRemove(String key, String value) {
        RScoredSortedSet<String> zSet = redissonClient.getScoredSortedSet(key);
        return zSet.remove(value);
    }

    /**
     * 获取 ZSet 中指定元素的分数
     *
     * @param key   zset key
     * @param value 元素
     * @return 分数，不存在则返回 null
     */
    public Double zSetGetScore(String key, String value) {
        RScoredSortedSet<String> zSet = redissonClient.getScoredSortedSet(key);
        return zSet.getScore(value);
    }

    /**
     * 获取 ZSet 剩余过期时间（单位：秒），  org.redisson.api.RExpirable#remainTimeToLive()时间单位返回是毫秒
     *
     * @param key ZSet 键
     * @return 剩余秒数；-1=永久；-2=key不存在
     */
    public long zSetGetExpireSeconds(String key) {
        RScoredSortedSet<String> zSet = redissonClient.getScoredSortedSet(key);
        long ttlMillis = zSet.remainTimeToLive();

        if (ttlMillis == -2) {
            return -2; // key 不存在
        }
        if (ttlMillis == -1) {
            return -1; // 永久有效
        }
        // 毫秒转秒（向下取整，符合 Redis 语义）
        return ttlMillis / 1000;
    }

}
