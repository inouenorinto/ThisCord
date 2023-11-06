package bean;

import java.io.Serializable;

public class UserBean implements Serializable{
	private String discord_user_id;
	private String mailaddress;
	private String password;
	private String user_name;
	private String user_icon;
	
	//Beanの直列化
	public UserBean() {}
	
	//discord_user_idのgetter,setter
	public String getDiscord_user_id() {
		return discord_user_id;
	}
	public void setDiscord_user_id(String discord_user_id) {
		this.discord_user_id = discord_user_id;
	}
	
	//mailaddressのgetter,setter
	public String getMailaddress() {
		return mailaddress;
	}
	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}
	
	//passwordのgetter,setter
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	//user_nameのgetter,setter
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	//user_iconのgetter,setter
	public String getUser_icon() {
		return user_icon;
	}
	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}
	
	
}
