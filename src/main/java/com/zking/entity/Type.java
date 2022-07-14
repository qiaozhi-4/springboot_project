package com.zking.entity;

<<<<<<<< HEAD:src/main/java/com/zking/entity/Img.java
import com.baomidou.mybatisplus.annotation.IdType;
========
>>>>>>>> origin/master:src/main/java/com/zking/entity/Type.java
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

<<<<<<<< HEAD:src/main/java/com/zking/entity/Img.java
========
import java.io.Serializable;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

//自动生成get/set/toString
>>>>>>>> origin/master:src/main/java/com/zking/entity/Type.java
@Data
//自动生成全参构造函数
@AllArgsConstructor
//自动生成无参构造函数
@NoArgsConstructor
<<<<<<<< HEAD:src/main/java/com/zking/entity/Img.java
@TableName("img")
public class Img {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String url;
========
@TableName("type")
public class Type implements Serializable {

    @TableId(type = AUTO)
    private Integer id;
    private String name;
>>>>>>>> origin/master:src/main/java/com/zking/entity/Type.java
}
