package com.ddd.example.infrastructure.utils;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 封装joda的时间工具，不用java自己的
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-05 19:52
 */
public class DateUtil {
    // 基础时间格式常量（根据你的实际业务调整，保持和原代码一致）
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_MILLISECOND_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";

    // 全局格式化器（线程安全，可复用）
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    private static final DateTimeFormatter DATE_MILLISECOND_FORMATTER = DateTimeFormatter.ofPattern(DATE_MILLISECOND_TIME_FORMAT);
    private static final DateTimeFormatter ISO_8601_FORMATTER = DateTimeFormatter.ofPattern(ISO_8601_FORMAT);

    // 固定时区（东八区，避免系统时区不一致问题）
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Shanghai");

    /**
     * 获取当前10位时间戳（秒级）
     *
     * @return 当前10位时间戳
     */
    public static long getCurrentTimestamp() {
        // 替换System.currentTimeMillis()，使用java.time更规范
        return Instant.now().getEpochSecond();
    }

    /**
     * 获取当前时间加上指定天数的日期
     *
     * @param days 天数（正数=未来，负数=过去，0=当前）
     * @return 当前时间加上指定天数的日期
     */
    public static Date getDateAfterDays(int days) {
        LocalDateTime current = LocalDateTime.now(DEFAULT_ZONE_ID);
        LocalDateTime target = current.plusDays(days);
        // LocalDateTime转Date（兼容旧返回值）
        return Date.from(target.atZone(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * 获取当前时间加上指定天数后的10位时间戳（秒级）
     *
     * @param days 天数
     * @return 当前时间加上指定天数后的10位时间戳
     */
    public static long getTimestampAfterDays(int days) {
        // 直接计算，避免多次Date转换，性能更优
        LocalDateTime current = LocalDateTime.now(DEFAULT_ZONE_ID);
        LocalDateTime target = current.plusDays(days);
        return target.atZone(DEFAULT_ZONE_ID).toInstant().getEpochSecond();
    }

    /**
     * 获取当前时间减去指定天数的日期
     *
     * @param days 天数
     * @return 当前时间减去指定天数的日期
     */
    public static Date getDateBeforeDays(int days) {
        LocalDateTime current = LocalDateTime.now(DEFAULT_ZONE_ID);
        LocalDateTime target = current.minusDays(days);
        return Date.from(target.atZone(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * 获取当前时间减去指定天数后的10位时间戳（秒级）
     *
     * @param days 天数
     * @return 当前时间减去指定天数后的10位时间戳
     */
    public static long getTimestampBeforeDays(int days) {
        LocalDateTime current = LocalDateTime.now(DEFAULT_ZONE_ID);
        LocalDateTime target = current.minusDays(days);
        return target.atZone(DEFAULT_ZONE_ID).toInstant().getEpochSecond();
    }

    /**
     * 将Date格式化为指定格式的字符串
     *
     * @param date 日期时间
     * @return 格式化后的日期时间字符串
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return null;
        }
        // Date转LocalDateTime，再格式化
        LocalDateTime dateTime = date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDateTime();
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    /**
     * 获取当前毫秒级时间戳并格式化为字符串（含毫秒）
     *
     * @return 格式化后的日期时间字符串（如：2026-03-23 17:00:00.123）
     */
    public static String formatDateTimeMilliSecondOfCurrent() {
        LocalDateTime current = LocalDateTime.now(DEFAULT_ZONE_ID);
        return DATE_MILLISECOND_FORMATTER.format(current);
    }

    /**
     * 生成ISO 8601格式的北京时间时间戳
     *
     * @return ISO 8601格式字符串（如：2026-03-23T17:00:00.123+0800）
     */
    public static String getISODateTimeOfCurrent() {
        ZonedDateTime current = ZonedDateTime.now(DEFAULT_ZONE_ID);
        return ISO_8601_FORMATTER.format(current);
    }

//    // 测试方法（验证功能一致性）
//    public static void main(String[] args) {
//        System.out.println("当前10位时间戳：" + getCurrentTimestamp());
//        System.out.println("3天后的日期：" + getDateAfterDays(3));
//        System.out.println("3天后的10位时间戳：" + getTimestampAfterDays(3));
//        System.out.println("2天前的日期：" + getDateBeforeDays(2));
//        System.out.println("2天前的10位时间戳：" + getTimestampBeforeDays(2));
//        System.out.println("当前时间格式化：" + formatDateTime(new Date()));
//        System.out.println("当前毫秒级格式化：" + formatDateTimeMilliSecondOfCurrent());
//        System.out.println("ISO 8601格式：" + getISODateTimeOfCurrent());
//    }


}