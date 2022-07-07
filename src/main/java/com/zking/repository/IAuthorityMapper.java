package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.Authority;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IAuthorityMapper extends BaseMapper<Authority> {
    //根据角色id查询，拥有哪些权限
    List<Authority> getAuthoritySByUserId(int id);
}
