package com.zking.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zking.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserMapper extends BaseMapper<User> {
    //根据giteeid查询用户的信息
    User getOneByGiteeId(String id);

}
