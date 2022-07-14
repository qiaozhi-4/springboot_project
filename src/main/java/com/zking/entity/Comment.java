package com.zking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zking.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
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
    private String type; // 弹幕类型，默认滚动
    private Boolean force; // 是否强制展示该弹幕

    public static Comment from(CommentDTO dto)
    {
        Comment msg = new Comment();
        msg.setUserId(dto.getUserId());
        msg.setConten(dto.getText());
        msg.setTime(new Timestamp(dto.getSendTime()));
        msg.setFilmId(dto.getMovieId());
        msg.setFilmTime(dto.getTime());
        msg.setColor(dto.getColor());
        msg.setBgColor(dto.getBgColor());
        msg.setType(dto.getType().getType());
        msg.setForce(dto.getForce());
        return msg;
    }

    public static CommentDTO to(Comment msg)
    {
        CommentDTO dto = new CommentDTO();
        dto.setUserId(msg.getUserId());
        dto.setMovieId(msg.getFilmId());
        dto.setSendTime(msg.getTime().getTime());
        dto.setBgColor(msg.getBgColor());
        dto.setText(msg.getConten());
        dto.setColor(msg.getColor());
        dto.setTime(msg.getFilmTime());
        dto.setType(CommentDTO.Type.valueOf(msg.getType().toUpperCase()));
        dto.setIsMe(false);
        dto.setForce(msg.getForce());
        return dto;
    }

}
