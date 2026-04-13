package com.ddd.example.adapter.controller.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 这个是用户的基本信息，用于以后日志打点分析用户行为的
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-16 14:39
 */
@Data
@Schema(title = "用户信息", description = "用户信息的请求信息，用户日志级别的详细记录")
public class UseInfo {
    /**
     * ip地址
     */
    @Schema(description = "用户ip")
    private String ip;
    /**
     * 网页端/API：1/2
     */
    @Schema(description = "用户来源,1-网页 2-api")
    private Integer channel;
    /**
     * 用户的cookie
     */
    @Schema(description = "用户cookie", required = false)
    private String cookie;
    /**
     * 页面账号；API接口可留空
     */
    @Schema(description = "用户id", required = false)
    private String userId;
    /**
     * 页面会话id（新开页面或点击新会话后更新）；API可留空
     */
    @Schema(description = "sessionId", required = false)
    private String sessionId;
    /**
     * 单轮会话id（串通全链路各服务日志）；未拆分推理服务和审核服务时用req_id
     */
    @Schema(description = "traceId")
    private String traceId;
    /**
     * 用户的accessKey
     */
    @Schema(description = "用户的accessKey")
    private String ak;
    /**
     * 非必传项目，没啥用，用于打点的
     */
    @Schema(description = "用户模型信息", required = false)
    private ModelTokenInfo modelTokenInfo;


    /**
     * 用户的模型信息，非必须填写（目前只有垃圾日志使用了）
     */
    @Data
    @Schema(title = "请求方的模型token", description = "请求方的模型token，用户日志级别的详细记录", required = false)
    public static class ModelTokenInfo {
        @Schema(description = "请求模型的id")
        private String modelId;
        @Schema(description = "最大token数目")
        private Integer maxTokens;
        @Schema(description = "模型的温度")
        private Double temperature;
        @Schema(description = "模型的topP")
        private Double topP;
        @Schema(description = "模型的topK")
        private Double topK;

    }


}
