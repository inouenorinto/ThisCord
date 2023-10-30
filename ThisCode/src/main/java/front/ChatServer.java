package front;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat/{room}")
public class ChatServer {
    private static final Map<String, Session> roomSessions = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("room") String room) {
        session.getUserProperties().put("room", room);
        roomSessions.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        String room = (String) session.getUserProperties().get("room");
        broadcast(room, session.getId() + ": " + message);
    }

    @OnClose
    public void onClose(Session session) {
        roomSessions.remove(session.getId());
    }

    private void broadcast(String room, String message) {
        for (Session session : roomSessions.values()) {
            if (room.equals(session.getUserProperties().get("room"))) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}