package com.zking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zking.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequiredArgsConstructor
@ServerEndpoint("/ws/{movieId}/{userId}")
public class WebSocketController
{
    // 这个实体类暂时没有什么价值，仅用于记录视频ID
    @Data @AllArgsConstructor
    static final class UserInfo
    {
        private Integer userId;
        private Integer movieId;
        private String socketSessionId;
    }
    
    // 记录人数，测试用
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();
    // 保存所有的用户信息，这些应该放到Service中
    public static final ConcurrentHashMap<Session, UserInfo> SESSION_USER_MAP = new ConcurrentHashMap<>();
    // 消息队列，高并发时用，暂时没用上
    private static final LinkedBlockingQueue<Comment> MESSAGE_QUEUE = new LinkedBlockingQueue<>();
    
    // WebSocket通信会话
    private Session session;
    
    // 广播消息
    public static void sendMessageToAll(Comment message) throws JsonProcessingException
    {
        // 肯定是登录用户
        assert  message.getUserId() != 0;
        // 只能发送文本信息
        String content = new ObjectMapper().writeValueAsString(message);
        // 遍历保存的会话给所有用户广播消息
        SESSION_USER_MAP.forEach((session, userInfo) -> {
            // 如果是自己发送的就不用广播了：这会导致一个用户同时打开相同的页面无法接收到自己的弹幕
            if (! Objects.equals(userInfo.getUserId(), message.getUserId()) // 这个判断没有用！没有更新userId，前端判断
                    && Objects.equals(message.getFilmId(), userInfo.getMovieId())) // 不是同一个视频就不用发送弹幕了
            {
                try
                {
                    session.getBasicRemote().sendText(content); // 发送
                } catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    // 这里是建立WebSocket链接时自动调用
    @OnOpen
    public void open(Session session, @PathParam("userId") Integer uid, @PathParam("movieId") Integer movieId)
    {
        int total = ATOMIC_INTEGER.getAndIncrement();
        System.out.printf("在线人数：%s%n", total);
        
        this.session = session;
        SESSION_USER_MAP.put(session, new UserInfo(uid, movieId, session.getId()));
    }
    
    // 关闭WebSockets时调用-测试
    @OnClose
    public void close()
    {
        int total = ATOMIC_INTEGER.getAndDecrement();
        System.out.printf("在线人数：%s%n", total);
        
        SESSION_USER_MAP.remove(session);
    }
    
    // WebSocket接收到消息-测试，不用WebSocket发送消息，用Post
    @OnMessage
    public void message(Session session, String message)
    {
        assert this.session == session;
        // 理论上不用WebSocket接收消息，而是推送消息，这里没用
        System.out.printf("收到来自【%s】（%s）的信息：%s%n", session.getId(), SESSION_USER_MAP.get(session), message);
    }
    
    // WebSocket发生错误时
    @OnError
    public void error(Session session, Throwable error)
    {
        assert this.session == session;
        System.out.printf("WebSocket发生异常！（%s）信息：%s%n", SESSION_USER_MAP.get(session), Arrays.toString(error.getStackTrace()));
    }
}
