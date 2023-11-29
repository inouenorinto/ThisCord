package endpoint;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import bean.ChannelBean;
import bean.JsonNoticeBean;
import bean.NoticeSessionBean;

@ServerEndpoint("/notice/{userId}/{userName}/{icon}")
public class NoticeServer {
	private static Map<Session, NoticeSessionBean> sessionsMap = new HashMap<>();
	//Integer: voiceChannelId
	private static Map<Integer, ChannelBean> channelsMap = new HashMap<>();
	
	@OnOpen
	public void onOpen(Session session, @PathParam("userId") int userId, @PathParam("userName") String userName,@PathParam("icon") String icon) {
        session.getUserProperties().put("userId", userId);
        session.getUserProperties().put("userName", userName);
        session.getUserProperties().put("icon", icon);
				
        NoticeSessionBean bean = new NoticeSessionBean();
		bean.setUserId(userId);
		bean.setUser(userName);
		bean.setIcon(icon);
		sessionsMap.put(session, bean);
		
		System.out.println("noticeServer オンライン:"+userName);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		Gson gson = new Gson();
    	
		JsonNoticeBean bean = null;
    	try {
    		bean = gson.fromJson(message, JsonNoticeBean.class);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	if(bean.getType().equals("joinVoiceChannel")) {
    		System.out.println(bean.getType());
    		
    		ChannelBean channelBean = new ChannelBean();
    		channelsMap.put(bean.getVoiceChannelid() );
    		
    	}
    	
		
	}

	@OnClose
	public void onClose(Session session) {

	}

	private void broadcastInChannel(int server_id, int channel_id, String message) {
		
	}

}
