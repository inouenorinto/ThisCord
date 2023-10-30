package front;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/websocket")
public class WebSocketEndpoint {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        // 認証ロジックをここに実装
        String username = (String) config.getUserProperties().get("username");
        String password = (String) config.getUserProperties().get("password");
        System.out.println(username);
    	System.out.println(password);
        
        if (authenticate(username, password)) {
        	System.out.println("成功");
            // 認証成功
        } else {
            // 認証失敗
            try {
            	System.out.println("失敗");
				session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "認証失敗"));
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    private boolean authenticate(String username, String password) {
        // ユーザー名とパスワードを検証するロジックをここに実装
        // 認証成功時はtrueを、認証失敗時はfalseを返す
    	String name = "admin";
    	String pass = "pass";
    	System.out.println(username);
    	System.out.println(password);
    	if(username.equals(name)&& password.equals(pass)) {
    		return true;
    	} else {
    		return false;
    	}
    }
}


