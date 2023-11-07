package bean;
import java.io.Serializable;

public class UserDataBean implements Serializable{
	String discord_user_id;
	String mailaddress;
	String password;
	String user_name;
	String user_icon;
	
	public String getDiscord_User_Id() {
		return discord_user_id;
	}
	
	public void setDiscord_User_Id(String discord_user_id) {
		this.discord_user_id = discord_user_id;
	}
	
	public String getMailaddress() {
		return mailaddress;
	}

	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_icon() {
		return user_icon;
	}

	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}


}
