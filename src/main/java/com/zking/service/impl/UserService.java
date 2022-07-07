package com.zking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zking.entity.Authority;
import com.zking.entity.Role;
import com.zking.entity.User;
import com.zking.repository.IAuthorityMapper;
import com.zking.repository.IRoleMapper;
import com.zking.repository.IUserMapper;
import com.zking.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//生成带有必需参数的构造函数，必需的参数是最终字段和具有约束的字段，例如@NonNull ，final
@RequiredArgsConstructor
@Service
public class UserService extends ServiceImpl<IUserMapper, User> implements IUserService {

    private final IRoleMapper roleMapper;
    private final IAuthorityMapper authorityMapper;

    //根据用户id查询，用户有哪些角色
    @Override
    public List<Role> getRolesByUserId(int id) {
        return roleMapper.getRolesByUserId(id);
    }

    //根据角色id查询，拥有哪些权限
    @Override
    public List<Authority> getAuthoritySByUserId(int id) {
        return authorityMapper.getAuthoritySByUserId(id);
    }





}
