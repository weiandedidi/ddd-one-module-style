package com.ddd.example.application.user;

import com.ddd.example.adapter.controller.query.UserListQuery;
import com.ddd.example.application.user.bo.UseInfoBo;
import com.ddd.example.domain.user.UserDomainService;
import com.ddd.example.domain.user.entity.UseInfoDomain;
import com.ddd.example.infrastructure.dao.UseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户的请求的应用类，Controller负责参数校验，application负责具体的逻辑（类似于个性化的逻辑的原有的mvc结构的service）
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-04 10:12
 */
@Service
@Slf4j
public class UserApplicationService {
    //调用domain层获取 用户领域的内容，也可以调用user的repository获取内容
    @Autowired
    UserDomainService userDomainService;

    public List<UseInfoBo> getList(UserListQuery listQuery) {
        UserDomainService.UserQueryBo queryBo = new UserDomainService.UserQueryBo();
        queryBo.setUserName(listQuery.getUserName());
        List<UseInfoDomain> useInfoDomains = userDomainService.getUseInfoDomain(queryBo);
        List<UseInfo> useInfoList = useInfoDomains.stream().map(UseInfoDomain::getUseInfo).collect(java.util.stream.Collectors.toList());
        return UseInfoBo.UserInfoCovert.INSTANCE.toUseInfoBoList(useInfoList);
    }


}
