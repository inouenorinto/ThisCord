package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bean.UserBean;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;
import util.mysql.MySqlManager;

public class ChatPageCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		UserBean sessionBean = (UserBean)req.getAttributeInSession("bean");
		UserBean bean = getRecord(sessionBean.getMailaddress());
		
    	req.setAttributeInSession("bean", bean);
		
		res.setTarget("/chat.html");

	}
	
	private UserBean getRecord(String email) {
		Connection cn = null;
		PreparedStatement  pstmt = null;
	    ResultSet rs = null;
	    String SQL="select * from user_data where mailaddress = ?";
	    
	    UserBean bean = new UserBean();
	    try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			
			if(rs != null && rs.next()) {
				
				bean.setUser_id(rs.getInt("user_id"));
				bean.setMailaddress(rs.getString("mailaddress"));
				bean.setPassword(rs.getString("password"));
				bean.setUser_name(rs.getString("user_name"));
				bean.setUser_icon(rs.getString("user_icon"));
				
				for(int num : getRooms(bean.getUser_id())) {
					System.out.println("getRecord: "+getRoomName(num));
					bean.addRooms(num, getRoomName(num));
				}
			}
			if (cn != null) {
				cn.close();
			}

            
		} catch(Exception e) {
			e.printStackTrace();
		}
	    return bean;
	}
	
	private String getRoomName(int id) {
		Connection cn = null;
		PreparedStatement  pstmt = null;
	    ResultSet rs = null;
	    String result = null;
	    String SQL = "select server_name from server_data where server_id = ?";
		try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			
			if(rs != null && rs.next()) {
				result = rs.getString("server_name");
			}
			if (cn != null) {
				cn.close();
			}

            
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private Integer[] getRooms(int user_id) {
		ArrayList<Integer> result = new ArrayList<>();
		
		Connection cn = null;
		PreparedStatement  pstmt = null;
	    ResultSet rs = null;
	    String SQL = "select server_id from user_server_relationship where user_id = ?";
	    try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery();
			
			if(rs != null) {
				while(rs.next()) {
					System.out.println("getRooms: "+rs.getInt("server_id"));
					result.add(rs.getInt("server_id"));
				}
			}
			if (cn != null) {
				cn.close();
			}

            
		} catch(Exception e) {
			e.printStackTrace();
		}
	    
	    return result.toArray(new Integer[result.size()]);
	}
}