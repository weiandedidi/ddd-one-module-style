package com.ddd.example.adapter.controller;

import com.ddd.example.adapter.controller.query.UserListQuery;
import com.ddd.example.adapter.controller.query.UserVo;
import com.ddd.example.application.user.UserApplicationService;
import com.ddd.example.application.user.bo.UseInfoBo;
import com.ddd.example.infrastructure.aspect.TraceLog;
import com.ddd.example.infrastructure.utils.JSONUtil;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author maqidi
 * @version 1.0
 * @create 2024-10-29 14:34
 */
@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {
    @Autowired
    UserApplicationService userApplicationService;

    /**
     * 获取list类
     *
     * @param query
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @Operation(summary = "用户列表", description = "用户的列表列表页请求，用于传递各种请求参数", method = "POST", tags = {"图片审核"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "列表页获取成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserVo.class)))
    })
    @TraceLog
    public ResponseVO<List<UserVo>> listUser(@Valid @RequestBody UserListQuery query) {
        //记性校验@Validated等，这里不校验了
        List<UseInfoBo> useInfoBos = userApplicationService.getList(query);
        List<UserVo> userVos = UserVo.UserVoCovert.INSTANCE.toUserVoList(useInfoBos);
        return ResponseVO.successResponse(userVos);
    }

    @RequestMapping(value = "hi", method = RequestMethod.GET)
    public ResponseVO<?> sayHi() {
        return ResponseVO.successEmptyResponse();
    }

    @PostMapping(value = "/student")
    public ResponseVO<?> sayHi(@RequestBody StudentRequest request) {

        // 关键：这里能看到前端传的【所有多余字段】
        System.out.println("多余字段 = " + request.getExtraFields());
        log.info("多余字段：{}", JSONUtil.toJsonString(request.getExtraFields()));
        return ResponseVO.successResponse(request);
    }

    @Data
    public static class StudentRequest{
        private String name;
        private Integer age;
        private final Map<String, Object> extraFields = new HashMap<>();
        /**
         * 核心注解：
         * 前端传的、类里没有的字段，都会自动进入这个方法
         */
        @JsonAnySetter
        public void set(String key, Object value) {
            extraFields.put(key, value);
        }

    }
}
