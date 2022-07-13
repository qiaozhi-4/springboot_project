package com.zking.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zking.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 判断用户名是否为空
        if (username == null || username.trim().isEmpty()) {
            // 用户名为空直接抛出异常
            throw new UsernameNotFoundException("用户名不能为空");
        }

        // 获取用户
        com.zking.entity.User user =
                userService.getOne(new QueryWrapper<com.zking.entity.User>().eq("username", username), false);
        // 判断数据库是否有该用户
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 查询该用户权限并赋予权限
        StringBuilder authorities = new StringBuilder();
        int count = 1;
        for (String s : userService.findAllJurisdictionByUserId(user.getId())) {
            if (count++ > 1) {
                authorities.append(",").append(s);
            }else {
                authorities.append(s);
            }
        }
        // 查询该用户角色并赋予权限
        for (String s : userService.findAllRoleByUserId(user.getId())) {
            if (count++ > 1) {
                authorities.append(",").append(s);
            }else {
                authorities.append(s);
            }
        }

        com.zking.entity.User u = new com.zking.entity.User();

        // User实现UserDetails接口
        u.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(String.valueOf(authorities)));
        u.setPass(encoder.encode(user.getPassword()));
        u.setUsername(username);
        u.setId(user.getId());
        u.setBirthday(user.getBirthday());
        // 返回该用户和权限security进行匹配
        return new User(user.getUsername(), user.getPass(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(authorities.toString()));
    }
}
