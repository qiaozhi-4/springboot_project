package com.zking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

//自动生成get/set/toString
@Data
//自动生成全参构造函数
@AllArgsConstructor
//自动生成无参构造函数
@NoArgsConstructor
@TableName("user")
public class User implements Serializable {
    @TableId(type = AUTO)
    private Integer id;
    private String username;
    @TableField("password")
    private String pass;
    @TableField("img_id")
    private int imgId;
    private String name;
    private String sex;
    private int vip;
    private Date birthday;
    private String address;
    private String email;
    private Date time;
    @TableField("last_login_time")
    private Date lastLoginTime;
}

