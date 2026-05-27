package com.ddd.example.adapter.controller;


import com.ddd.example.infrastructure.utils.JSONUtil;
import com.ddd.example.infrastructure.utils.RedisBaseUtils;
import com.ddd.example.infrastructure.valueobject.BizErrorCodeEnum;
import com.ddd.example.infrastructure.valueobject.RedisUseCaseEnum;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 展示redis使用的Controller后门类
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-15 20:24
 */
@Slf4j
@RestController
@RequestMapping("/tools/util")
public class RedisToolsBackController {
//    @Resource
    private RedisBaseUtils redisBaseUtils;
    @Resource
    private RedissonClient redissonClient;


    /**
     * k8s的心跳检测
     */
    @GetMapping("/health")
    public ResponseVO<?> health() {
        return ResponseVO.successEmptyResponse();
    }

    @PostMapping("/redis/zAdd")
    public ResponseVO<zSetResponse> zAddRedis(@RequestBody ZSetInfo zSetInfo) {
        log.info("tool zAddRedis {}", JSONUtil.toJsonString(zSetInfo));
        if (Objects.isNull(zSetInfo)) {
            return ResponseVO.failure(BizErrorCodeEnum.PARAM_VALIDATION_ERROR);
        }
        boolean isSuccess = redisBaseUtils.zSetAdd(zSetInfo.getKey(), zSetInfo.getValue(), System.currentTimeMillis(), zSetInfo.getMinutes(), TimeUnit.MINUTES);
        return getAndBuildzSetResponseVo(zSetInfo);
    }

    @NotNull
    private ResponseVO<zSetResponse> getAndBuildzSetResponseVo(@RequestBody ZSetInfo zSetInfo) {
        Double score = redisBaseUtils.zSetGetScore(zSetInfo.getKey(), zSetInfo.getValue());
        zSetResponse zSetResponse = new zSetResponse();
        zSetResponse.setKey(zSetInfo.getKey());
        zSetResponse.setValue(zSetInfo.getValue());
        zSetResponse.setScore(score);
        zSetResponse.setExpireSeconds(redisBaseUtils.zSetGetExpireSeconds(zSetInfo.getKey()));
        return ResponseVO.successResponse(zSetResponse);
    }

    @PostMapping("/redis/zRemove")
    public ResponseVO<zSetResponse> zRemove(@RequestBody ZSetInfo zSetInfo) {
        log.info("tool zRemove {}", JSONUtil.toJsonString(zSetInfo));
        if (Objects.isNull(zSetInfo)) {
            return ResponseVO.failure(BizErrorCodeEnum.PARAM_VALIDATION_ERROR);
        }
        redisBaseUtils.zSetRemove(zSetInfo.getKey(), zSetInfo.getValue());
        return getAndBuildzSetResponseVo(zSetInfo);
    }

    @PostMapping("/redis/zGetScore")
    public ResponseVO<zSetResponse> zGetScore(@RequestBody ZSetInfo zSetInfo) {
        log.info("tool zGetScore {}", JSONUtil.toJsonString(zSetInfo));
        if (Objects.isNull(zSetInfo)) {
            return ResponseVO.failure(BizErrorCodeEnum.PARAM_VALIDATION_ERROR);
        }
        return getAndBuildzSetResponseVo(zSetInfo);
    }


    @GetMapping("/redis/deleteKey")
    public ResponseVO<Boolean> deleteKey(String key) {
        return ResponseVO.successResponse(redisBaseUtils.deleteString(key));
    }

    /**
     * 分布式锁的使用案例
     * @param key
     * @return
     */
    @GetMapping("/redis/tryLock")
    public ResponseVO<String> tryLock(String key) {
        String lockKey = RedisBaseUtils.generateKey(key, RedisUseCaseEnum.DISTRIBUTED_LOCK);
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(0, 30, TimeUnit.SECONDS);
            if (!locked) {
                return ResponseVO.successResponse("获取锁失败");
            }
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.info("tryLock error", e);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return ResponseVO.successResponse("锁定后解锁成功");
    }


    @Data
    public static class ZSetInfo {
        private String key;
        private String value;
        private int minutes;
    }

    @Data
    public static class zSetResponse {
        private String key;
        private String value;
        private Double score;
        /**
         * 过期时间秒
         */
        private Long expireSeconds;
    }


}
