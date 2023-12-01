package endpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import bean.JsonNoticeBean;
import bean.NoticeSessionBean;

@ServerEndpoint("/notice/{userId}/{userName}/{icon}")
public class NoticeServer {
	private static Map<Integer,Set<Session>> serverSession = new HashMap<>();
	private static Map<Integer,Set<Session>> voiceChannelSession = new HashMap<>();
	private static Map<Session,NoticeSessionBean> notices = new HashMap<>();
	
	@OnOpen
	public void onOpen(Session session, @PathParam("userId") int userId, @PathParam("userName") String userName,@PathParam("icon") String icon) {
        session.getUserProperties().put("userId", userId);
        session.getUserProperties().put("userName", userName);
        session.getUserProperties().put("icon", icon);
        
        NoticeSessionBean bean = new NoticeSessionBean();
        bean.setUserId(userId);
		bean.setUser(userName);
		bean.setIcon(icon);

		notices.put(session, bean);
		
		System.out.println("noticeServer オンライン:"+userName);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		Gson gson = new Gson();
		JsonNoticeBean bean = null;
		int serverId = 0;
		int voiceChannelId = 0;
		
    	try {
    		bean = gson.fromJson(message, JsonNoticeBean.class);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	serverId = bean.getServerId();    	
    	voiceChannelId = bean.getVoiceChannelid(); 
    	if(bean.getType().equals("joinServer")) {//サーバーに入ったとき
    		System.out.println("NoticeServer.type :"+bean.getType());
    		session.getUserProperties().put("serverId", serverId);

    		serverSession.computeIfAbsent(serverId, k -> ConcurrentHashMap.newKeySet()).add(session);
    		System.out.println("noticeServer オンライン数:"+serverSession.get(serverId).size());
    	}else if(bean.getType().equals("joinVoiceChannel")) {//ビデオチャンネルに入ったとき
    		System.out.println("NoticeServer.type :"+bean.getType());
    		session.getUserProperties().put("voiceChannelId", voiceChannelId);
    		
    		voiceChannelSession.computeIfAbsent(voiceChannelId, k -> ConcurrentHashMap.newKeySet()).add(session);
    		
    		JsonNoticeBean mess = JsonInitChannel(serverId,voiceChannelId );
    		String json = gson.toJson(mess);
    		System.out.println(json);
    		sendServer(serverId, json);
    	}else if(bean.getType().equals("disconnectVoiceChannel")) {
    		System.out.println("NoticeServer.type :"+bean.getType());
    		
    		voiceChannelSession.getOrDefault(voiceChannelId, Collections.emptySet()).remove(session);
    		
    		JsonNoticeBean mess = JsonInitChannel(serverId,voiceChannelId );
    		String json = gson.toJson(mess);
    		System.out.println(json);
    		sendServer(serverId, json);
    	}
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
	    System.out.println(reason);
	    Integer serverId = (Integer) session.getUserProperties().get("serverId");  // キーを "sessionId" から "serverId" に変更
	    Integer voiceChannelId = (Integer) session.getUserProperties().get("voiceChannelId");
	    serverSession.getOrDefault(serverId, Collections.emptySet()).remove(session);
	    voiceChannelSession.getOrDefault(voiceChannelId, Collections.emptySet()).remove(session);

	    notices.remove(session);

	    System.out.println("noticeServer 切断 オンライン数:" + serverSession.size());
	    serverSession.size();
	}

	/* 接続エラーが発生したとき */
	@OnError
	public void onError(Session session, Throwable t) {
	    System.out.println("エラーが発生しました。");

	    int serverId = (int) session.getUserProperties().get("serverId");
	    int voiceChannelId = (int) session.getUserProperties().get("voiceChannelId");
	    serverSession.getOrDefault(serverId, Collections.emptySet()).remove(session);
	    voiceChannelSession.getOrDefault(voiceChannelId, Collections.emptySet()).remove(session);

	    notices.remove(session);

	    t.printStackTrace();
	}

	
	public JsonNoticeBean JsonInitChannel(int serverId, int channelId) {
		JsonNoticeBean mess = new JsonNoticeBean();
		mess.setServerId(serverId);
		mess.setVoiceChannelid(channelId);
		
		Set<Session> sessions = voiceChannelSession.getOrDefault(serverId, Collections.emptySet());
		
		for(Session ses: sessions) {
			mess.getMembers().add(notices.get(ses));
		}
		return mess;
	}
	
	public void sendServer(int serverId, String json) {
		Set<Session> sessions = serverSession.getOrDefault(serverId, Collections.emptySet());;
		for(Session session : sessions) {
			try {
				session.getBasicRemote().sendText(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	

}