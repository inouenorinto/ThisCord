package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.ServerInfoDTO;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;
import db.mysql.MySqlManager;

public class InvitationCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		int user_id = Integer.parseInt(req.getParameter("user_id")[0]);
		ServerInfoDTO bean = (ServerInfoDTO) req.getAttributeInSession("serverInfo");
		int server_id = bean.getServer_id();
		
		invaite(server_id, user_id);
		
		res.setTarget("fn/chat");
	}
	
	private void invaite(int server_id, int user_id) {
		Connection cn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "INSERT INTO user_server_relationship (user_id, server_id) VALUES (?, ?);";

		try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, server_id);
			
			pstmt.executeUpdate();
			
			if (cn != null) {
				cn.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
