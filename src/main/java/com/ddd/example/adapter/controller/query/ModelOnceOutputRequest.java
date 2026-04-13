package com.ddd.example.adapter.controller.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @author maqidi
 * @version 1.0
 * @create 2024-09-03 17:50
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor()
@Schema(title = "模型的一次性输出校验请求", description = "chat类的用于用户输入内容判定，是否安全合规，内部会进行多轮对话压缩")
public class ModelOnceOutputRequest {
    /**
     * 用户的信息
     */
    @Schema(description = "用户信息", required = true)
    @Valid
    private UseInfo useInfo;

    @Schema(description = "待校验文本", required = true)
    @NotEmpty(message = "请求的内容体不为空")
    private String content;

    @Schema(description = "是否测评使用", required = false)
    private Boolean isTest = false;

    @Schema(description = "模板编码", required = false)
    private String templateId;
}
