package com.ddd.example.infrastructure.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 这里的基础组件的拦截处理
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-05 19:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TraceLog {
}
