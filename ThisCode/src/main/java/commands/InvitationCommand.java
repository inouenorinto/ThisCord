package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.mysql.MySqlManager;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;
import util.Sanitizer;

public class InvitationCommand extends AbstractCommand {
	String yellow = "\u001b[00;33m";
	String end    = "\u001b[00m";
	
	@Override
	public void execute(RequestContext req, ResponseContext res) {
		int user_id = Integer.parseInt(Sanitizer.sanitizing(req.getParameter("userId")[0]));
		int server_id = Integer.parseInt(Sanitizer.sanitizing(req.getParameter("serverId")[0]));
		int id = Integer.parseInt(req.getParameter("id")[0]);
		System.out.println(yellow+"InvitationCommnd.java "+ end +": ユーザID:"+ user_id + "が、サーバID" + server_id+"に招待されました。");
		invaite(server_id, user_id);
		
		String deviceType = req.getDeviceType();
		String target = null;
		if(deviceType == "Smartphone") {
			target = "spchat.html";
		} else {
			target ="chat.html";
		} 
		
		res.setRedirect("/"+target+"?id="+ id);
	}
	
	private void invaite(int server_id, int user_id) {
		Connection cn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "INSERT INTO us_relationship (user_id, server_id) VALUES (?, ?);";

		try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, server_id);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MySqlManager.close();
		}
	}

}
