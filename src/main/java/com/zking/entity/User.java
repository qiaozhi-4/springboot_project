package com.zking.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

//自动生成get/set/toString
@Data
//自动生成全参构造函数
@AllArgsConstructor
//自动生成无参构造函数
@NoArgsConstructor
@TableName("user")
public class User  implements UserDetails {
    @TableId(type = AUTO)
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String headImg;
    private String name;
    private Character sex;
    private Integer vip;
    private String vipTime;
    private String birthday;
    private String address;
    private String email;
    private String time;
    private String lastLoginTime;

    @TableField(exist = false)
    private List<GrantedAuthority> authorities;

    @Override
    public String getUsername() {
        return username;
    }

    //继承后必须返回为true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //不能返回null
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    //默认使用恒等去判断是否是同一个对象，因为登录的同一个用户，如果再次登录就会封装
    //一个新的对象，这样会导致登录的用户永远不会相等，所以需要重写equals方法
    @Override
    public boolean equals(Object obj) {
        //会话并发生效，使用username判断是否是同一个用户

        if (obj instanceof User) {
            //字符串的equals方法是已经重写过的
            return ((User) obj).getUsername().equals(this.username);
        } else {
            return false;
        }


    }
}

