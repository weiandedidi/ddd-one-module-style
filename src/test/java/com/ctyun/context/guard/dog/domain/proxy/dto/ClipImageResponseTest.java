package com.ddd.example.domain.proxy.dto;

import com.ddd.example.infrastructure.utils.GsonUtil;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class ClipImageResponseTest {

    @Test
    public void testNormalCovert() {
        //正常返回
        String json = "{\"code\":0,\"message\":\"success\",\"result\":[{\"suggestion\":\"block\",\"block\":[{\"type\":\"test@baokong\",\"text\":\"命中的违禁的文字\",\"score\":0.8,\"qid\":\"450276136302989395\",\"thresh\":0.6}]},{\"suggestion\":\"block\",\"block\":[{\"type\":\"test@baokong\",\"text\":\"命中的违禁的文字\",\"score\":0.7,\"qid\":\"450276136302989396\",\"thresh\":0.6}]}]}";
        ClipImageResponse response = GsonUtil.fromJsonString(json, ClipImageResponse.class);
        System.out.println(GsonUtil.toJsonString(response));
    }

    @Test
    public void testFailCovert() {
        String json = "{\"code\":500001,\"message\":\"服务接口异常，请联系管理员\",\"details\":\"服务接口异常，需要联系管理员处理\"}";
        ClipImageResponse response = GsonUtil.fromJsonString(json, ClipImageResponse.class);
        System.out.println(GsonUtil.toJsonString(response));
    }

}