package com.ddd.example.infrastructure.utils;

import com.ddd.example.infrastructure.utils.httpdto.JsonStringHttpResponse;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class HttpClientUtilTest {


    @Test
    public void doGetJsonRequest() {
        String url = "http://127.0.0.1:8080/user/get";
        Map<String, String> paramsMap = Maps.newHashMap();
        paramsMap.put("id", "5");
        JsonStringHttpResponse response = HttpClientUtil.doGetJsonRequest(url, paramsMap);
        assertThat(response).isNotNull();
        User user = GsonUtil.fromJsonString(response.getBody(), User.class);
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getAge());
        System.out.println(user.getAddress());
    }

    //测试ok
    @Test
    public void sendPostRequest() {
        String url = "http://127.0.0.1:8080/user/update";
        User user = new User(99L, "userA", 18, "测试地址");
        JsonStringHttpResponse response = HttpClientUtil.sendPostRequest(url, GsonUtil.toJsonString(user));
        assertThat(response).isNotNull();
        User resultUser = GsonUtil.fromJsonString(response.getBody(), User.class);
        System.out.println(resultUser.getId());
        System.out.println(resultUser.getName());
        System.out.println(resultUser.getAge());
        System.out.println(resultUser.getAddress());

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        //id
        private Long id;
        //名字
        private String name;
        //年龄
        private Integer age;
        //地址
        private String address;

    }
}