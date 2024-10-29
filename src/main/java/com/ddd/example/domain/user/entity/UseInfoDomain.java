package com.ddd.example.domain.user.entity;

import com.ddd.example.infrastructure.dao.UseInfo;
import lombok.Data;

/**
 * 用户的实体类，用于封装数据库中表的字段，或者作为领域实体，封装聚合后的领域对象
 * 例如 用户 + 详细账号的描述 =  用户领域
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-10-29 15:11
 */
@Data
public class UseInfoDomain {
    private UseInfo useInfo;
    private String address;
    /**
     * 用户的地址的街道poi，放在用户地址的表中，需要在地址表中查找。
     */
    private String street;

}
