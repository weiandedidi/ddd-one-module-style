package com.ddd.example.adapter.controller.cmd;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 存放写操作的请求
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-10-29 14:32
 */
@Schema(title = "用户更新请求", description = "用户更新请求")
@Data
public class UserUpdateCmd {

    @Schema(description = "用户id")
    private String userId;
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
