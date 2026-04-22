package com.ddd.example.adapter.controller;

import com.ddd.example.adapter.controller.query.ModelOnceOutputRequest;
import com.ddd.example.adapter.controller.query.TextModerationResponse;
import com.ddd.example.adapter.controller.query.UserOnceInputRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author maqidi
 * @version 1.0
 * @create 2024-09-03 17:33
 */
@Slf4j
@RestController
@RequestMapping("/text")
@Validated
@Tags({@Tag(name = "文本审核护栏", description = "处理文本审核的入口，用于判断文本是否符合安全规定")})
public class TextModerationController {


    /**
     * swagger3 Operation带有tag才能被 划分到指定的tag下
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/once/moderate", method = RequestMethod.POST)
    @Operation(summary = "用户一次性输入文字审核", description = "推理服务调用的，用户一次性输入文字审核，内部会有多轮对话的压缩，是一个定制化的接口，非普通文本审核", method = "POST", tags = {"文本审核护栏"})
    public ResponseVO<TextModerationResponse> userOnceInputTextModerate(@Valid @RequestBody UserOnceInputRequest request) {
        TextModerationResponse response = new TextModerationResponse();
        response.setIsPass(true);
        return new ResponseVO<>(200, "success", response);
    }


    /**
     * swagger3 Operation带有tag才能被 划分到指定的tag下
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/model/once/moderate", method = RequestMethod.POST)
    @Operation(summary = "模型一次性输出的文字审核", description = "推理服务调用的，模型一次性输出的文字审核，内部会有多轮对话的压缩，是一个定制化的接口，非普通文本审核", method = "POST", tags = {"文本审核护栏"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "文本审核成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TextModerationResponse.class)))
    })
    public ResponseVO<TextModerationResponse> modelOnceOutputTextModerate(@Valid @RequestBody ModelOnceOutputRequest request) {
        TextModerationResponse response = new TextModerationResponse();
        response.setIsPass(true);
        return new ResponseVO<>(200, "success", response);
    }


    //服务存活的检验方法
    @RequestMapping(value = "/do", method = RequestMethod.GET)
    public String health() {
        return "OK";
    }


}
