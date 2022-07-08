package com.zking.service.impl;

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
    private final IUserMapper userMapper;

    //根据用户id查询，用户有哪些角色
    public List<String> findAllRoleByUserId(int id) {
        return userMapper.findAllRoleByUserId(id);
    }

    //根据角色id查询，拥有哪些权限
    public List<String> findAllJurisdictionByUserId(int id) {
        return userMapper.findAllJurisdictionByUserId(id);
    }





}
