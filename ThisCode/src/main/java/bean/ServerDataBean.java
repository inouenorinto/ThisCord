package bean;

import java.io.Serializable;

public class ServerDataBean implements Serializable {
    private String discordServerId;
    private String serverName;
    private String hostId;
    private String serverIcon;
    private String serverMemberId;

    public ServerDataBean() {
        // Default constructor
    }

    public String getDiscordServerId() {
        return discordServerId;
    }

    public void setDiscordServerId(String discordServerId) {
        this.discordServerId = discordServerId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getServerIcon() {
        return serverIcon;
    }

    public void setServerIcon(String serverIcon) {
        this.serverIcon = serverIcon;
    }

    public String getServerMemberId() {
        return serverMemberId;
    }

    public void setServerMemberId(String serverMemberId) {
        this.serverMemberId = serverMemberId;
    }
}
