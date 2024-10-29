package com.ddd.example.adapter.controller.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * user请求的list队列，满足CQRS的风格
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-10-29 14:27
 */
@Data
@Schema(title = "user的列表页请求", description = "列表页请求体")
public class UserListQuery {
    /**
     * 以下是请求的字段
     */
    @Schema(description = "用户名")
    private String userName;
    @Schema(description = "用户状态")
    private Integer status;
    @Schema(description = "用户角色")
    private Integer role;
    @Schema(description = "用户性别")
    private Integer gender;
    @Schema(description = "用户年龄")
    private Integer age;
}
