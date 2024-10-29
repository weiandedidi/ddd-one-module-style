package com.ddd.example.infrastructure.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户的实体类，用于封装数据库中表的字段，或者作为领域实体，封装聚合后的领域对象
 * 例如 用户 + 详细账号的描述 =  用户领域
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-10-29 15:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UseInfo {
    private String userId;
    private String userName;
    private Integer status;
    private Integer role;
    private Integer gender;
    private Integer age;
}
