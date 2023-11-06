package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.UserBean;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;
import util.mysql.MySqlManager;

public class LoginCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		String username = req.getParameter("username")[0];
		String password = req.getParameter("password")[0];
		if(isLoginValid(username, password)) {
			UserBean bean = new UserBean();
        	bean.setUsername(username);
        	bean.setPassword(password);
        	bean.addRooms("room1");
        	bean.addRooms("room2");
        	
        	req.setAttributeInSession("bean", bean);
			res.setTarget("/chat.html");
			MySqlManager.getConnection();
		} else {
			res.setTarget("/login.html");
		}
		
	}
	private boolean isLoginValid(String username, String password) {
		Connection cn = null;
		PreparedStatement  pstmt = null;
	    ResultSet rs = null;
	    String SQL = "select user_name, password from user_data where user_name = ?";
	    boolean flag = false;
		try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setString(1, username);
			rs = pstmt.executeQuery();
			
			if(rs != null && rs.next()) {
				rs.getString("user_name");
				if(username.equals(rs.getString("user_name")) && password.equals(rs.getString("password"))) {
					flag =true;
				}
			}
            
		} catch(Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
