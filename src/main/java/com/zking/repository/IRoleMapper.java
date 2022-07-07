package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRoleMapper extends BaseMapper<Role> {
    //根据用户id查询，用户有哪些角色
    List<Role> getRolesByUserId(int id);
}
