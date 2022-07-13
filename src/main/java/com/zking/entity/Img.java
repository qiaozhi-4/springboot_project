package com.zking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//自动生成全参构造函数
@AllArgsConstructor
//自动生成无参构造函数
@NoArgsConstructor
@TableName("img")
public class Img {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String url;
}
