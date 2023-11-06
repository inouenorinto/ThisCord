package bean;

import java.util.ArrayList;

public class UserBean {
	private ArrayList<String> rooms = new ArrayList<>();
	private String password;
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<String> getRooms() {
		return rooms;
	}

	public void addRooms(String room) {
		this.rooms.add(room);
	}
}
