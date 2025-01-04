package Quiz.QuizWebApplication.Config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class QuizWebSocketHandler extends TextWebSocketHandler {
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
      sessions.add(session);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    }
    public void notifyAllClients(String statusMessage) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(statusMessage));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void sendNextQuestion(String question) {
        notifyAllClients("Next Question: " + question);
    }

}
