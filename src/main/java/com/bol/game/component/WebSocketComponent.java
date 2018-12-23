package com.bol.game.component;

import com.bol.game.pojo.WebSocketMessage;
import com.bol.game.utility.JsonUtil;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Manages websocket connections and publishing boards via websocket messages.
 * 
 * @author Taner Aruk
 *
 */
@Slf4j
@Component
public class WebSocketComponent extends TextWebSocketHandler implements WebSocketHandler {

    private final GameComponent      gameComponent;
    private       WebSocketSession[] sessions;

    public WebSocketComponent(final GameComponent gameComponent) {

        this.sessions = new WebSocketSession[2];
        this.gameComponent = gameComponent;
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession closedSession, final CloseStatus status) {

        log.info("Websocket connection closed [{}]", closedSession.getRemoteAddress());

        try {
            for (final WebSocketSession session : sessions) {
                session.close();
            }

            sessions = new WebSocketSession[2];
        } catch (IOException e) {
            log.error("Error occurred", e);
            log.error("Error occured while closing sessions");
        }
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession connectedSession) {

        log.info("New web socket connection established [{}]", connectedSession.getRemoteAddress());

        try {
            if (sessions[GameComponent.FIRST_PLAYER] == null) {
                sessions[GameComponent.FIRST_PLAYER] = connectedSession;
            } else if (sessions[GameComponent.SECOND_PLAYER] == null) {
                sessions[GameComponent.SECOND_PLAYER] = connectedSession;
                gameComponent.start();
                publishBoard();
            } else { // Do not allow more than 2 users
                connectedSession.close();
            }
        } catch (Exception e) {
            log.error("Error occurred", e);
            log.error(
                "Could not send websocket message to -> [{}] - {}", connectedSession.getRemoteAddress(), e.getMessage()
            );
        }
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {

        gameComponent.select(Integer.parseInt(message.getPayload()));
        publishBoard();
    }

    private void publishBoard() throws IOException {

        for (int i = 0; i < sessions.length; i++) {
            final WebSocketSession session = sessions[i];

            final WebSocketMessage message = new WebSocketMessage();
            message.setBoard(gameComponent.getBoard());
            message.setConnectedUser(i);

            session.sendMessage(new TextMessage(JsonUtil.toJson(message)));
        }
    }
}