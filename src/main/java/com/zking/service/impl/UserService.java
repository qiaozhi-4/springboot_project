package com.zking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zking.entity.User;
import com.zking.repository.IUserMapper;
import com.zking.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//生成带有必需参数的构造函数，必需的参数是最终字段和具有约束的字段，例如@NonNull ，final
@RequiredArgsConstructor
@Service
public class UserService extends ServiceImpl<IUserMapper, User> implements IUserService {

    //给用户添加角色
    @Override
    public Boolean addRole() {
        return getBaseMapper().addRole()!=null;
    }

    @Override
    //根据用户id查询，用户有哪些角色
    public List<String> findAllRoleByUserId(int id) {
        return getBaseMapper().findAllRoleByUserId(id);
    }

    // 根据用户id查询，用户有哪些权限
    @Override
    public List<String> findAllAuthoritySByUserId(int id) {
        return getBaseMapper().findAllAuthoritySByUserId(id);
    }

    //登录【根据用户名查询用户】
    @Override
    public User findUserByUsername(String userName) {
        return getOne(new QueryWrapper<User>().eq("username", userName));
    }


}
