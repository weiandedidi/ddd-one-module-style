package com.ddd.example.infrastructure.utils;

import com.ddd.example.infrastructure.utils.httpdto.HttpWarpperResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * 统计方法调用的工具类，这个方法最好用在Application和domain层，因为防腐层上面使用这个方法才有必要，构造层自己需要的结果类
 * 我们更关心防腐层的proxy返回值，底层http的结果在http工具中打印即可，上层不需要感知，可以做统一的bizException封装
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-31 16:14
 */
@Slf4j
public class HttpTimeSpanCallableUtil {

    public static <T> HttpWarpperResponse<T> executeWithTiming(Supplier<T> method, String methodName, HttpWarpperResponse<T> defaultExceptionResponse) {
        long startTime = System.currentTimeMillis();
        T result = null;
        HttpWarpperResponse<T> response;
        try {
            result = method.get();
            response = new HttpWarpperResponse<>(result, System.currentTimeMillis() - startTime, null);
            //外面打印结果，这里就不打印结果了
            return response;
        } catch (Exception e) {
            log.error(" {} net error, traceId {}, e", methodName, TraceIdUtil.getTraceId(), e);
            return defaultExceptionResponse;
        }
    }
}
