package com.zking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
同一个用户可以同时播放不同ID的视频
因此会话ID也可以不一样
即使同一个用户、同一个视频，不同网页打开也是可以的
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class WebSocketUser
{
    private Integer userId;
    private Integer movieId;
    private String socketSessionId;
}
