package com.zking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
//自动生成全参构造函数
@AllArgsConstructor
//自动生成无参构造函数
@NoArgsConstructor
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer filmId;
    private String conten;
    private Date time;
    private Double filmTime; // 发送弹幕时视频的播放时间
    private String color; // 字体前景色
    private String bgColor; // 字体背景色



}
