package com.zking.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zking.dto.CommentDTO;
import com.zking.entity.WebSocketUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketService
{
    // 获取自定义的线程池
    private final Executor executor;
    // 记录人数，测试用
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();
    // 保存所有的用户信息
    private static final ConcurrentHashMap<Session, WebSocketUser> SESSION_USER_MAP = new ConcurrentHashMap<>();
    // 消息队列，高并发时用
    private static final LinkedBlockingQueue<CommentDTO> MESSAGE_QUEUE = new LinkedBlockingQueue<>();
    private static boolean stopServer = false;

    // 获取信息
    public void saveSession(Session session, WebSocketUser user)
    {
        // 可以判断下是否有这么一个session然后删除后再put
        SESSION_USER_MAP.put(session, user);
    }

    public void removeSession(Session session)
    {
        SESSION_USER_MAP.remove(session);
    }

    public WebSocketUser getUser(Session session)
    {
        return SESSION_USER_MAP.get(session);
    }

    // 统计人数的方法
    public int increaseOnline()
    {
        return ATOMIC_INTEGER.incrementAndGet();
    }

    public int decreaseOnline()
    {
        return ATOMIC_INTEGER.decrementAndGet();
    }

    public int getOnlineTotal()
    {
        return ATOMIC_INTEGER.get();
    }

    // 消息相关，停止和发送
    @Async
    public void sendMessage(CommentDTO message)
    {
        try
        {
            MESSAGE_QUEUE.put(message);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e); // 消息发送失败，报错！
        }
    }

    public static void stop()
    {
        stopServer = true;
    }

    // 异步调用
    @Async
    public void startSendingService()
    {
        log.info("====> 开启消息发送（无限循环）");
        while (!stopServer)
        {
            try
            {
                CommentDTO message = MESSAGE_QUEUE.take();
                // 这里最好开启线程池调用，返回Future
                executor.execute(() -> sendMessageToAll(message));
            } catch (InterruptedException ignored) { /*被打断后继续*/ }
        }
    }

    private void sendMessageToAll(CommentDTO message)
    {
        // 肯定是登录用户
        assert message.getUserId() != 0;
        try
        {
            // 只能发送文本信息
            String content = new ObjectMapper().writeValueAsString(message);
            // 遍历保存的会话给所有用户广播消息
            SESSION_USER_MAP.forEach((session, userInfo) -> {
                // 如果是自己发送的就不用广播了：这会导致一个用户同时打开相同的页面无法接收到自己的弹幕
                if (!Objects.equals(userInfo.getUserId(), message.getUserId()) // 如果没有更新userId，也就是没有登录，那么这个判断没用
                        && Objects.equals(message.getMovieId(), userInfo.getMovieId())) // 不是同一个视频就不用发送弹幕了
                {
                    try
                    {
                        session.getBasicRemote().sendText(content); // 发送
                    } catch (IOException ignored) { /*发送失败忽略，可以加日志*/ }
                }
            });
        } catch (JsonProcessingException ignored) { }
    }
}
