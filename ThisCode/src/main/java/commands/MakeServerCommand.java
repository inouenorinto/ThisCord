package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import bean.UserBean;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;
import util.mysql.MySqlManager;

public class MakeServerCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		System.out.println("kokomadekitoru");
		String server_name = req.getParameter("server_name")[0];
		System.out.println("server_name: "+server_name);
		UserBean bean = (UserBean)req.getAttributeInSession("bean");
		System.out.println("userid: "+bean.getUser_id());
		makeServer(server_name,  bean.getUser_id());
		
		res.setTarget("fn/chat");
	}
	private boolean makeServer(String server_name, int user_id) {
		boolean flag = false;
		
		Connection cn = null;
		PreparedStatement  pstmt = null;
		Statement st = null;
	    ResultSet rs = null;
	    String SQL="INSERT INTO server_data (server_name, host_id, server_icon) VALUES (?, ?, ?)";
		String updateSQL ="INSERT INTO user_server_relationship (user_id, server_id) VALUES (?, ?);";
	    
		try {
			
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setString(1, server_name);
			pstmt.setInt(2, user_id);
			pstmt.setString(3, "icon_url");
			pstmt.executeUpdate();
			
			st = cn.createStatement();
			rs = st.executeQuery("select max(server_id) AS server_id from server_data");
			
			int index = 0;
			if(rs.next()) {
				index = rs.getInt("server_id");
			}
			
			pstmt = null;
			pstmt = cn.prepareStatement(updateSQL);
			
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, index);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return flag;
	}
}


