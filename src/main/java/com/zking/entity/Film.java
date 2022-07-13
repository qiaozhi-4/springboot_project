package com.zking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField("file_id")
    private Integer fileId;
    private String info;
    @TableField("img_id")
    private Integer imgId;
    private String actor;
    private String director;
    private Integer vip;
    private Date time;
    private Double score;
    private Long heat;
    private Integer state;


}
