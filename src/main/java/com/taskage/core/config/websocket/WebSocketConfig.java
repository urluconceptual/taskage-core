package com.taskage.core.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Bean
    public TeamWebSocketHandler teamWebSocketHandler() {
        return new TeamWebSocketHandler();
    }

    @Bean
    public TaskWebSocketHandler taskWebSocketHandler() {
        return new TaskWebSocketHandler();
    }

    @Bean
    public UserWebSocketHandler userWebSocketHandler() {
        return new UserWebSocketHandler();
    }

    @Bean
    public SprintWebSocketHandler sprintWebSocketHandler() {
        return new SprintWebSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(teamWebSocketHandler(), "/core/ws/team")
                .addHandler(taskWebSocketHandler(), "/core/ws/task")
                .addHandler(userWebSocketHandler(), "/core/ws/user")
                .addHandler(sprintWebSocketHandler(), "/core/ws/sprint")
                .setAllowedOrigins("*");
    }
}