package com.zking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zking.entity.User;

import java.util.List;

public interface IUserService extends IService<User> {

    //根据giteeid查询用户的信息
    // 根据用户id查询，用户有哪些角色
    List<String> findAllRoleByUserId(int id);

    // 根据用户id查询，用户有哪些权限
    List<String> findAllAuthoritySByUserId(int id);

    //登录【根据用户名查询用户】
    User findUserByUsername(String userName);
}
