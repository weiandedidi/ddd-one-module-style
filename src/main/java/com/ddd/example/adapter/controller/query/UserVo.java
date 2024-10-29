package com.ddd.example.adapter.controller.query;

import com.ddd.example.application.user.bo.UseInfoBo;
import com.ddd.example.domain.user.entity.UseInfoDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户的vo类，用于屏蔽底层user实体类有些不展示的字段，并拓展一些实体没有的字段（例如父子类表中的信息）
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-10-29 14:40
 */
@Data
public class UserVo {
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

    @Mapper
    public interface UserVoCovert {
        UserVo.UserVoCovert INSTANCE = Mappers.getMapper(UserVo.UserVoCovert.class);

        UserVo toUserVo(UseInfoBo useInfoBo);

        List<UserVo> toUserVoList(List<UseInfoBo> useInfoBos);

    }
}
