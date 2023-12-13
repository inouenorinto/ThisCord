package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.mysql.MySqlManager;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class InvitationCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		int user_id = Integer.parseInt(req.getParameter("userId")[0]);
		int server_id = Integer.parseInt(req.getParameter("serverId")[0]);
		System.out.println("InvitationCommnd.java : userid "+ user_id + " server_id" + server_id);
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
