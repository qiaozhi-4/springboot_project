package com.zking.security;

import com.zking.entity.User;
import com.zking.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 判断用户名是否为空
        if (username == null || username.trim().isEmpty()) {
            // 用户名为空直接抛出异常
            throw new UsernameNotFoundException("用户名不能为空");
        }

        // 获取用户
        User user = userService.findUserByUsername(username);

        // 判断数据库是否有该用户
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 查询该用户权限并赋予权限
        StringBuilder authorities = new StringBuilder();
        int count = 1;
        for (String s : userService.findAllAuthoritySByUserId(user.getId())) {
            if (count++ > 1) {
                authorities.append(",").append(s);
            } else {
                authorities.append(s);
            }
        }
        // 查询该用户角色并赋予权限
        for (String s : userService.findAllRoleByUserId(user.getId())) {
            if (count++ > 1) {
                authorities.append(",").append(s);
            } else {
                authorities.append(s);
            }
        }

        //获取Vip到期时间
        String vipTime = user.getVipTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //当前时间
        try {
            Date vip = dateFormat.parse(vipTime);
            Date date = new Date();
            if (vip.compareTo(date) < 0) {
                System.out.println("vip 时间在 date 之前");
                user.setVip(0);
                userService.saveOrUpdate(user);

            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // User实现UserDetails接口
        user.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(String.valueOf(authorities)));

        // 返回该用户和权限security进行匹配
        return user;
    }
}
