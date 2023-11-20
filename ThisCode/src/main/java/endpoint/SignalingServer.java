package endpoint;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class SignalingServer {

    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);

        System.out.println("rr" + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {

        broadcast(message, session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("Client disconnected: " + session.getId());
    }

    private void broadcast(String message, Session Mysession) {
    	int i = 0;
        for (Session session : sessions) {
            try {
            	if(session != Mysession) {
            		session.getBasicRemote().sendText(message);
            		i++;
            	}
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        System.out.println("count: "+ i);
    }
}