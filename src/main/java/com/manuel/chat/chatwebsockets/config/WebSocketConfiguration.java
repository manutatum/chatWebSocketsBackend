package com.manuel.chat.chatwebsockets.config;

import com.manuel.chat.chatwebsockets.service.ChatWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatHandler;

    public WebSocketConfiguration(ChatWebSocketHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/ws")
                .setAllowedOrigins("*");
    }
}
