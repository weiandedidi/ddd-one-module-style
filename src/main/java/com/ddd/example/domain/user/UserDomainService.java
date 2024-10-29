package com.ddd.example.domain.user;

import com.ddd.example.domain.user.entity.UseInfoDomain;
import com.ddd.example.infrastructure.dao.UseInfo;
import com.ddd.example.infrastructure.dao.UseInfoExample;
import com.ddd.example.infrastructure.dao.UserMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 示例类，用于展示领域服务的内容
 *
 * @author maqidi
 * @version 1.0
 * @create 2024-07-04 09:30
 */
@Service
public class UserDomainService {

//    @Autowired
    UserMapper userMapper;

    /**
     * 需要具体的请求参数类进行处理，不能仅仅写 userApplication的请求参数，应为其他的Application可能调用这个方法获取user
     *
     * @return
     */
    public List<UseInfoDomain> getUseInfoDomain(UserQueryBo queryBo) {
        //bo转为dao中的查询参数
        UseInfoExample example = new UseInfoExample();
        example.setUserId(queryBo.getUserId());
        example.setUserName(queryBo.getUserName());
//        List<UseInfo> useInfoList = userMapper.getList(example);
        //进行use的地址查找和包装， 这里简略了，不写地址了
        List<UseInfo> useInfoList = createDemoList();
        return useInfoList.stream().map(useInfo -> {
            //找地址信息
            UseInfoDomain userInfoDomain = new UseInfoDomain();
            userInfoDomain.setUseInfo(useInfo);
            userInfoDomain.setStreet("demo");
            userInfoDomain.setAddress("测试的");
            return userInfoDomain;
        }).collect(Collectors.toList());
    }

    @Data
    public static class UserQueryBo {
        private String userId;
        private String userName;
    }

    private List<UseInfo> createDemoList() {
        List<UseInfo> users = new ArrayList<>();
        // 创建5个UseInfo对象
        users.add(new UseInfo("1", "张三", 1, 2, 1, 25));
        users.add(new UseInfo("2", "李四", 1, 1, 0, 30));
        users.add(new UseInfo("3", "王五", 0, 2, 1, 22));
        users.add(new UseInfo("4", "赵六", 1, 1, 0, 35));
        users.add(new UseInfo("5", "孙七", 0, 2, 1, 28));
        return users;
    }
}
