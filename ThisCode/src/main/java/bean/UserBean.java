package bean;

import java.util.HashMap;
import java.util.Map;

public class UserBean {
	private Map<Integer, String> rooms = new HashMap<>();
    private int user_id; 
    private String mailaddress;
    private String password;
    private String user_name;
    private String user_icon;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getMailaddress() {
		return mailaddress;
	}

	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<Integer, String> getRooms() {
		return rooms;
	}

	public void addRooms(int room_id, String room_name) {
		this.rooms.put(room_id, room_name);
	}
}
