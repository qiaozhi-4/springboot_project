package com.zking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


// WebSocket配置
@Configuration
@EnableWebSocket
// @EnableAsync
public class WebSocketConfig /*implements WebSocketConfigurer*/
{
    // 唯一配置
    //@Bean
    public ServerEndpointExporter exporter()
    {
        return new ServerEndpointExporter();
    }

    // 暂时没用
    /*@Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        registry.addHandler(new SessionWebSocketHandler(), "/session")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }*/
}
