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
@TableName("film")
public class Film {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String mp4Src;
    private String info;
    private String imgSrc;
    private String actor;
    private String director;
    private Integer vip;
    private Date time;
    private Double score;
    private Integer heat;
    private String region;
    private String coverSrc;


}
