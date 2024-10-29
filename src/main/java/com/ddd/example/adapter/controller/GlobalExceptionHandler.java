package com.ddd.example.adapter.controller;

import com.ddd.example.infrastructure.valueobject.BizErrorCodeEnum;
import com.ddd.example.infrastructure.utils.TraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

/**
 * controller层全局异常捕获
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-11 16:19
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 参数校验异常捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseVO<Object> error(ConstraintViolationException e) {
        log.error("traceId {}, constraintViolationException e, ", TraceIdUtil.getTraceId(), e);
        return ResponseVO.failure(BizErrorCodeEnum.PARAM_VALIDATION_ERROR);
    }

    /**
     * 断言类的参数校验不通过
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVO<Object> error(MethodArgumentNotValidException e) {
        log.error("traceId {}, MethodArgumentNotValidException e, ", TraceIdUtil.getTraceId(), e);
        return ResponseVO.failure(BizErrorCodeEnum.PARAM_VALIDATION_ERROR);
    }

    //方法不支持，如post不支持get
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseVO<Object> error(org.springframework.web.HttpRequestMethodNotSupportedException e) {
        log.error("traceId {}, org.springframework.web.HttpRequestMethodNotSupportedException e, ", TraceIdUtil.getTraceId(), e);
        return ResponseVO.failure(BizErrorCodeEnum.METHOD_NOT_SUPPORTED);
    }
}
