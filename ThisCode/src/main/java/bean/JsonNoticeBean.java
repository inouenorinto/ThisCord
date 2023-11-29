package bean;

public class JsonNoticeBean {
	private String type;
	private int voiceChannelid;
	private String user;
	private String icon;
	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getVoiceChannelid() {
		return voiceChannelid;
	}

	public void setVoiceChannelid(int voiceChannelid) {
		this.voiceChannelid = voiceChannelid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
