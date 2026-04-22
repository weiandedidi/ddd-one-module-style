package com.ddd.example.adapter.controller.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author maqidi
 * @version 1.0
 * @create 2024-09-03 17:41
 */
@Data
@Schema(title = "用户一次性输入请求", description = "chat类的用于用户输入内容判定，是否安全合规，内部会进行多轮对话压缩")
public class UserOnceInputRequest {
    /**
     * 用户的信息
     */
    @Schema(description = "用户信息", required = true)
    @Valid
    private UseInfo useInfo;

    @Valid
    @Schema(description = "待校验文本集合", required = true)
    @NotEmpty(message = "待校验文本集合不为空")
    private List<ContentInfo> contentInfoList;

    @Schema(description = "是否测评使用", required = false)
    private Boolean isTest = false;

    @Schema(description = "模板编码", required = false)
    private String templateId;

    @Schema(title = "用户一次性输入请求内容体", description = "chat类的用于用户输入内容判定，是否安全合规，内部会进行多轮对话压缩")
    @Data
    @NoArgsConstructor
    public static class ContentInfo {
        @Schema(description = "输入的内容", required = true)
        @JsonProperty("content")
        private String content;

        @Schema(description = "用户的角色，openai中 user、system等", required = true)
        @NotBlank(message = "输入角色不为空")
        @JsonProperty("role")
        private String role;
    }
}
