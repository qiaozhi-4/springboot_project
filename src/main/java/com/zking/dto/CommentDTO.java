package com.zking.dto;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO 弹幕【前端】实体类
@Data @AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO
{
    public enum Type
    {
        @JsonEnumDefaultValue TOP("top"), BOTTOM("bottom"), SCROLL("scroll");

        private final String type;
        Type(String type)
        {
            this.type = type;
        }

        @Override
        public String toString()
        {
            return type;
        }

        @JsonValue
        public String getType()
        {
            return type;
        }
    }

    private String text; // 弹幕文字
    private String color; // 弹幕颜色
    private Double time; // 弹幕出现时间
    private String  type; // 弹幕类型，默认滚动
    private Boolean isMe; // 是否是当前用户发送的
    private Boolean force; // 是否强制展示该弹幕

    // 自定义字段，也要添加一个userName，显示在前端
    private Integer userId; // 用户ID
    private Integer movieId; // 哪个视频
    private Long sendTime; // 发送的时间
    private String bgColor; // 弹幕颜色

}
