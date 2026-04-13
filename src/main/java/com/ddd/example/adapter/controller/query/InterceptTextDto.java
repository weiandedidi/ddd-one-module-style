package com.ddd.example.adapter.controller.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author maqidi
 * @version 1.0
 * @create 2024-09-03 18:58
 */
@Data
@AllArgsConstructor
@Schema(title = "文字护栏的结果", description = "文字护栏的结果")
public class InterceptTextDto {
    /**
     * 拦截的模块名，有的是正则，黑白名单，不是算法，所以定义成module模块
     */
    @Schema(description = "拦截的模块名称")
    private String moduleName;
    /**
     * 命中的词汇
     */
    @Schema(description = "命中的词汇")
    private List<String> hitWords;

    @Schema(description = "模块命中的code值")
    private List<Integer> hitCodeList;
    /**
     */
    @Schema(description = "结果说明 0-内容判定不拦截，1-超时不拦截，2-算法开关关闭不拦截，3-业务系统异常不拦截，4-内容判定拦截", required = true)
    private String interceptTypeDesc;

}
