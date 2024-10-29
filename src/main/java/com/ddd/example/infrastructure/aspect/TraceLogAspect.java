package com.ddd.example.infrastructure.aspect;

import com.ddd.example.infrastructure.utils.GsonUtil;
import com.ddd.example.infrastructure.utils.TraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

/**
 * 写一个traceId的拦截器，用于打印指定日志的格式形式
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-01 11:30
 */
@Aspect
@Component
@Slf4j
public class TraceLogAspect {
    @Around("@annotation(TraceLog)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();


        RequestAttributes ra = RequestContextHolder.getRequestAttributes();

        String url = null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) ra;
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest httpServletRequest = requestAttributes.getRequest();
            url = httpServletRequest.getRequestURL().toString();
        }

        String traceId = TraceIdUtil.getTraceId();
        String request = GsonUtil.toJsonString(joinPoint.getArgs());
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("TraceId: {} - Method: {} - Exception: {}", traceId, methodName, throwable.getMessage());
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            //这个即使为null，也会打印
            String response = GsonUtil.toJsonString(result);
            //打印入参和出参
            log.info("traceId: {} - url: {} - method: {} - Execution time: {} ms - request: {} - response: {} ", traceId, url, methodName, executionTime, request, response);
        }
        return result;
    }
}
