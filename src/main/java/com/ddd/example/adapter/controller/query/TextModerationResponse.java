package com.ddd.example.adapter.controller.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author maqidi
 * @version 1.0
 * @create 2024-09-03 17:50
 */
@Data
@Schema(title = "文字审核结果", description = "文字审核的结果")
public class TextModerationResponse {
    /**
     * 审核是否通过，true通过
     */
    @Schema(description = "审核是否通过,true通过", required = true)
    private Boolean isPass;

    @Schema(description = "拦截的算法的具体内容", required = false)
    List<InterceptTextDto> interceptTextDtoList;
}
