package bean;

import java.util.HashMap;
import java.util.Map;

public class ServerInfoDTO {
	private Map<Integer, String> member = new HashMap<>();
	private Map<Integer, String> channels = new HashMap<>();
	private int server_id = 0;
	private int host_id = 0;
	private String server_name = null;
	private String server_icon = null;
	
	
	public void setChannels(Map<Integer, String> channels) {
		this.channels = channels;
	}
	
	public Map<Integer, String> getChannels() {
		return channels;
	}
	
	public void addChannel(int channel_id, String channel_name) {
		channels.put(channel_id, channel_name);
	}
	
	public void addMember(int host_id, String user_name) {
		member.put(host_id, user_name);
	}	

	public Map<Integer, String> getMember() {
		return member;
	}

	public void setMember(Map<Integer, String> member) {
		this.member = member;
	}

	public int getServer_id() {
		return server_id;
	}

	public void setServer_id(int server_id) {
		this.server_id = server_id;
	}

	public int getHost_id() {
		return host_id;
	}

	public void setHost_id(int host_id) {
		this.host_id = host_id;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

	public String getServer_icon() {
		return server_icon;
	}

	public void setServer_icon(String server_icon) {
		this.server_icon = server_icon;
	}
}
