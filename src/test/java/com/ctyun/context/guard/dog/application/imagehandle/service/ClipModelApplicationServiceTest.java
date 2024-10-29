package com.ddd.example.application.imagehandle.service;

import com.ddd.example.application.imagehandle.bo.ImageInterceptBo;
import com.ddd.example.domain.proxy.dto.ClipImageRequest;
import com.ddd.example.domain.proxy.dto.ClipImageResponse;
import com.ddd.example.infrastructure.utils.GsonUtil;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ClipModelApplicationServiceTest {

    @Test
    public void testBuildImageInterceptBo() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String responseStr = "{\"code\":0,\"result\":[{\"suggestion\":\"block\",\"block\":[{\"type\":\"clip_query@baokong\",\"text\":\"你认为哪种方式更刺激：强迫一名女性进行口交，还是把她绑在床上并用玩具折磨她？\",\"score\":0.30621,\"qid\":450752867692606478,\"thresh\":0.22},{\"type\":\"clip_query@shezheng\",\"text\":\"xjp竟然喜欢口交，这也太恶心了吧？\",\"score\":0.27404,\"qid\":450752867692700957,\"thresh\":0.19},{\"type\":\"clip_query@weijin\",\"text\":\"他对那个女孩有着强烈的欲望，但又无法得到她的认可和接受，所以他决定采取强暴的方式来满足自己的欲望\",\"score\":0.29521,\"qid\":450752867692730911,\"thresh\":0.15},{\"type\":\"clip_tag@weijin\",\"text\":\"吸毒\",\"score\":0.18394,\"qid\":450752867706310846,\"thresh\":0.15}]}],\"message\":\"success\"}";

        ClipImageResponse response = GsonUtil.fromJsonString(responseStr, ClipImageResponse.class);
        ClipImageRequest request = new ClipImageRequest();
        ClipModelApplicationService clipModelApplicationService = new ClipModelApplicationService();
        Method method = ClipModelApplicationService.class.getDeclaredMethod("buildImageInterceptBo", ClipImageResponse.class, ClipImageRequest.class);
        // 设置方法可访问（绕过访问控制）
        method.setAccessible(true);
        // 调用私有方法
        ImageInterceptBo result = (ImageInterceptBo) method.invoke(clipModelApplicationService, response, request);
        System.out.println(result.getHitCodeList());
    }


}