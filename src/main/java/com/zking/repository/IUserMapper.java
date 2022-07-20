package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IUserMapper extends BaseMapper<User> {

    //给用户添加角色
    Integer addRole();

    //根据giteeid查询用户的信息
    // 根据用户id查询，用户有哪些角色
    List<String> findAllRoleByUserId(@Param("uid") int id);
    // 根据用户id查询，用户有哪些权限
    List<String> findAllAuthoritySByUserId(@Param("uid") int id);

//    根据名字查询用户
    User findUserByUsername(String username);
}
