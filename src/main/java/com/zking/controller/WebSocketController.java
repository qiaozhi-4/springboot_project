package com.zking.controller;

import com.zking.entity.WebSocketUser;
import com.zking.service.impl.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;

@Controller
@ServerEndpoint("/ws/{movieId}/{userId}")
@Slf4j
public class WebSocketController implements ApplicationContextAware
{
    /*
    这里无法直接注入，
    因为WebSocket会不断创建新的@ServerEndpoint WebSocketController对象，
    注意使用static静态变量，共享同一个对象
    */
    private static WebSocketService socketService;
    // 记录人数，测试用
    // WebSocket通信会话
    private Session session;

    // 这里是建立WebSocket链接时自动调用
    @OnOpen
    public void open(Session session, @PathParam("userId") Integer uid, @PathParam("movieId") Integer movieId)
    {
        int total = socketService.increaseOnline();
        this.session = session;
        socketService.saveSession(session, new WebSocketUser(uid, movieId, session.getId()));
        log.info("【上线】在线人数：{}", total);
    }

    // 关闭WebSockets时调用
    @OnClose
    public void close()
    {
        int total = socketService.decreaseOnline();
        socketService.removeSession(session);
        log.info("【下线】在线人数：{}", total);
    }

    // WebSocket接收到消息-【测试，可以删除】，不用WebSocket发送消息，用Post
    @OnMessage
    public void message(Session session, String message)
    {
        assert this.session == session;
        // 不用WebSocket接收消息，而是推送消息，这里没用
        log.warn("【恶意API调用】收到来自{}（{}）的信息：{}", session.getId(), socketService.getUser(session), message);
    }

    // WebSocket发生错误时-【测试，可以删除】
    @OnError
    public void error(Session session, Throwable error)
    {
        assert this.session == session;
        log.error("【异常】WebSocket发生异常！（{}）信息：{}", socketService.getUser(session), Arrays.toString(error.getStackTrace()));
    }

    // 使用ApplicationContext注入
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        socketService = applicationContext.getBean(WebSocketService.class);
        socketService.startSendingService();
    }
}
