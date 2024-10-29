package com.ddd.example.infrastructure.dao;

import java.util.List;

/**
 * mybatis的用户映射类
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-10-29 15:25
 */
public interface UserMapper {
    List<UseInfo> getList(UseInfoExample useInfoExample);

}
