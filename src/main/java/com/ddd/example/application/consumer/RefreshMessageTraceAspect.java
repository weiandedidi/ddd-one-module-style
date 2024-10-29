package com.ddd.example.application.consumer;

import com.ddd.example.infrastructure.config.message.MessageHeader;
import com.ddd.example.infrastructure.utils.GsonUtil;
import com.ddd.example.infrastructure.utils.TraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * 刷新消息的日志切面，消息使用的使用打开注解
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-05 19:37
 */
//@Aspect
//@Component
@Slf4j
public class RefreshMessageTraceAspect {
    @Around("execution(* com.ddd.example.application.consumer.RefreshMessageListener.onMessage(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length > 1 && args[1] instanceof String) {
            String message = (String) args[1];
            MessageHeader header = GsonUtil.fromJsonString(message, MessageHeader.class);
            if (null == header) {
                log.error("MessageHeader is null, message {}", message);
                return joinPoint.proceed();
            }
            //空就生成,不用option，因为空也是需要生成id的。
            String traceId = null;
            if (StringUtils.isNoneEmpty(header.getTraceId())) {
                traceId = header.getTraceId();
            }
            traceId = Optional.ofNullable(traceId).orElse(TraceIdUtil.generateTraceId());
            //日志写入
            TraceIdUtil.setTraceId(traceId);
        }
        try {
            return joinPoint.proceed();
        } finally {
            TraceIdUtil.clear();
        }
    }
}
