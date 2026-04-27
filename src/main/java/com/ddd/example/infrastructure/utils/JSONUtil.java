package com.ddd.example.infrastructure.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * ackson 工具类（替代 GsonUtil）
 *
 * @author maqidi
 * @version 1.0
 * @create 2026-03-24 11:19
 */
@Slf4j
public class JSONUtil {
    // 全局 ObjectMapper 实例（线程安全）
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true).configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true).configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true).setTimeZone(TimeZone.getDefault());
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        OBJECT_MAPPER.registerModule(javaTimeModule);
    }

    /**
     * JSON字符串转Object对象（对应Gson的fromJsonString），泛型对象处理
     */
    public static <T> T fromJsonString(String jsonStr, TypeReference<T> typeReference) {
        if (jsonStr == null) {
            return null;
        }
        if (StringUtils.isBlank(jsonStr)) {
            throw new IllegalArgumentException("jsonStr 不能为空字符");
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Jackson 解析 JSON 字符串失败, jsonStr={}", jsonStr, e);
            throw new IllegalArgumentException("JSON 反序列化失败", e);
        }
    }

    /**
     * 普通对象处理
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJsonString(String jsonStr, Class<T> clazz) {
        if (jsonStr == null) {
            return null;
        }
        if (StringUtils.isBlank(jsonStr)) {
            throw new IllegalArgumentException("jsonStr 不能为空字符");
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            log.error("Jackson 解析 JSON 失败, targetClass={}", clazz.getName(), e);
            throw new IllegalArgumentException("JSON 反序列化失败", e);
        }
    }

    /**
     * 将对象序列化为 JSON 字符串
     */
    public static String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Jackson 序列化对象为 JSON 失败, objectType={}", obj.getClass().getName(), e);
            throw new IllegalArgumentException("对象序列化为 JSON 失败", e);
        }
    }

}
