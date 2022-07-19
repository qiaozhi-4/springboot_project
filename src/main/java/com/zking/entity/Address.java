package com.zking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;



@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@TableName("country_state_city")
public class Address
{
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("pid")
    private Integer parentId;
    private Integer deep;
    
    private String name;
    @TableField("pinyin_prefix")
    private String pinyinPrefix;
    private String pinyin;
    @TableField("ext_id")
    private String extId;
    @TableField("ext_name")
    private String extName;
}
