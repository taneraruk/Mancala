package com.bol.game.config;

import com.bol.game.component.WebSocketComponent;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Websocket server configuration.
 * 
 * @author Taner Aruk
 *
 */
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    private WebSocketComponent webSocketComponent;

    public WebsocketConfig(final WebSocketComponent webSocketComponent) {
        this.webSocketComponent = webSocketComponent;
    }

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketComponent, "/game").setAllowedOrigins("*");
    }
}