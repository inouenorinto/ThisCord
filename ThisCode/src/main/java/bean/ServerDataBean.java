package bean;

import java.io.Serializable;

public class ServerDataBean implements Serializable {
    private int server_id;
    private String server_name;
    private int host_id;
    private String server_icon;
    
    public ServerDataBean() {}
    
	public int getServer_id() {
		return server_id;
	}
	public void setServer_id(int server_id) {
		this.server_id = server_id;
	}
	public String getServer_name() {
		return server_name;
	}
	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}
	public int getHost_id() {
		return host_id;
	}
	public void setHost_id(int host_id) {
		this.host_id = host_id;
	}
	public String getServer_icon() {
		return server_icon;
	}
	public void setServer_icon(String server_icon) {
		this.server_icon = server_icon;
	}

}