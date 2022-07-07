package com.zking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zking.entity.Authority;
import com.zking.entity.Role;
import com.zking.entity.User;

import java.util.List;

public interface IUserService extends IService<User>{

    //根据用户id查询，用户有哪些角色
    List<Role> getRolesByUserId(int id);

    //根据角色id查询，拥有哪些权限
    List<Authority> getAuthoritySByUserId(int id);


}
