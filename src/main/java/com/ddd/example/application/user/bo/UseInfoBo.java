package com.ddd.example.application.user.bo;

import com.ddd.example.domain.user.entity.UseInfoDomain;
import com.ddd.example.infrastructure.dao.UseInfo;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 这个是用户的基本信息，用于以后日志打点分析用户行为的可以封装不同场景的用于
 * <p>
 * 例如 需要订单的行为分析   需要  用户类 + 订单类的聚合，  拿到订单的详细信息，并且获取到用户的详细信息
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-16 14:39
 */
@Data
public class UseInfoBo {
    private String userId;
    private String userName;
    private Integer status;
    private Integer role;
    private Integer gender;
    private Integer age;

    @Mapper
    public interface UserInfoCovert {
        UserInfoCovert INSTANCE = Mappers.getMapper(UserInfoCovert.class);

        UseInfoBo toUseInfoBo(UseInfo useInfo);

        List<UseInfoBo> toUseInfoBoList(List<UseInfo> useInfoList);

    }


}
