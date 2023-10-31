package front;
import java.io.IOException;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat/{room}/{username}")
public class ChatServer {
    @OnOpen
    public void onOpen(Session session, @PathParam("room") String room, @PathParam("username") String username) {
    	System.out.println("接続開始");
    	System.out.println("username" + username);
        session.getUserProperties().put("room", room);
        session.getUserProperties().put("username", username);
        RoomSessionManager.addSession(room, session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        String room = (String) session.getUserProperties().get("room");
        String username = (String) session.getUserProperties().get("username");
        System.out.println(room+"."+ username + ": " + message);
        
        broadcast(room, username + ": " + message);
    }

    @OnClose
    public void onClose(Session session) {
    	System.out.println("接続破棄");
        String room = (String) session.getUserProperties().get("room");
        RoomSessionManager.removeSession(room, session);
    }

    private void broadcast(String room, String message) {
        Set<Session> sessions = RoomSessionManager.getSession(room);
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

