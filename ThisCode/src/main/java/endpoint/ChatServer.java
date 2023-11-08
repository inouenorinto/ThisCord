package endpoint;
import java.io.IOException;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat/{server_id}/{channel_id}/{user_id}")
public class ChatServer {
    @OnOpen
    public void onOpen(Session session, @PathParam("server_id") String s_server_id, @PathParam("channel_id") String s_channel_id,@PathParam("user_id") String s_user_id) {
    	System.out.println("接続開始");
    	System.out.println("user_id" + s_server_id);
    	System.out.println("channel" + s_channel_id);
    	System.out.println("user_id" + s_user_id);
    	int server_id = Integer.parseInt(s_server_id);
    	int channel_id = Integer.parseInt(s_channel_id);
    	int user_id = Integer.parseInt(s_user_id);
        session.getUserProperties().put("server_id", server_id);
        session.getUserProperties().put("channel_id", channel_id);
        session.getUserProperties().put("user_id", user_id);
        
        ServerSessionBean bean = new ServerSessionBean();
        bean.setServer_id(server_id);
        bean.setUser_id(user_id);
        bean.addSession(channel_id ,session);
        
        ServerSessionManager.addSession(server_id, bean);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        int server_id = (int) session.getUserProperties().get("server_id");
        int channel_id = (int) session.getUserProperties().get("channel_id");
        int user_id = (int) session.getUserProperties().get("user_id");
        System.out.println(server_id+"."+ user_id + ": " + message);
        
        broadcast(server_id, channel_id, message);
    }

    @OnClose
    public void onClose(Session session) {
    	System.out.println("接続破棄");
        int sever_id = (int) session.getUserProperties().get("server_id");
        int channel_id = (int) session.getUserProperties().get("channel_id");
        ServerSessionManager.removeSession(sever_id, channel_id, session);
    }

    private void broadcast(int server_id, int channel_id, String message) {
        Set<Session> sessions = ServerSessionManager.getSession(server_id, channel_id);
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


