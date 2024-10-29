package com.ddd.example.infrastructure.utils.httpdto;

import com.ddd.example.infrastructure.valueobject.BizException;
import jdk.nashorn.internal.runtime.URIUtils;

/**
 * http方法的包装类，用于封装业务获得结果后的http方法类
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-31 16:24
 */
public class HttpWarpperResponse<T> {
    /**
     * 返回的结果
     */
    private T result;
    /**
     * 执行时间
     */
    private long executionTime;
    /**
     * 业务异常，默认返回系统调用异常
     */
    private BizException bizException;

    public HttpWarpperResponse(T result, long executionTime, BizException bizException) {
        this.result = result;
        this.executionTime = executionTime;
        this.bizException = bizException;
    }

    public T getResult() {
        return result;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public BizException getBizException() {
        return bizException;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public void setBizException(BizException bizException) {
        this.bizException = bizException;
    }

    /**
     * 统一封装网络异常返回
     *
     * @param bizException
     * @param <T>          返回值的定义
     * @return
     */
    public static <T> HttpWarpperResponse<T> buildNetFailResponse(BizException bizException) {
        return new HttpWarpperResponse<>(null, -1, bizException);
    }
}
