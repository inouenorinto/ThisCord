package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.google.gson.Gson;

import bean.ServerInfoDTO;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;
import util.mysql.MySqlManager;

public class GetServerInfoCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		int roomId = Integer.parseInt(req.getParameter("roomId")[0]);
		System.out.println("roomId: " + roomId);
		ServerInfoDTO dto = getBean(roomId);
		req.setAttributeInSession("serverInfo", dto);
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		System.out.println("json: "+ new Gson().toJson(dto));
		res.getWriter().write(new Gson().toJson(dto));
	}

	private ServerInfoDTO getBean(int roomId) {
		ServerInfoDTO bean = new ServerInfoDTO();
		Connection cn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "select * from user_server_relationship where server_id= ?";
		String selectSQL="select * from server_data where server_id= ?";

		try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setInt(1, roomId);
			rs = pstmt.executeQuery();
			if (rs !=null) {
				while(rs.next()) {
					System.out.println("user_id: "+rs.getInt("user_id"));
					bean.addMember(rs.getInt("user_id"), getUserName(rs.getInt("user_id")));
				}
			}
			
			pstmt = cn.prepareStatement(selectSQL);
			pstmt.setInt(1, roomId);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				bean.setHost_id(rs.getInt("host_id"));
				bean.setServer_id(roomId);
				bean.setServer_name(rs.getString("server_name"));
				bean.setServer_icon(rs.getString("server_icon"));
			}
			
			if (cn != null) {
				cn.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	private String getUserName(int id) {
		Connection cn = null;
		PreparedStatement  pstmt = null;
	    ResultSet rs = null;
	    String result = null;
	    String SQL = "select user_name from user_data where user_id = ?";
		try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			
			if(rs != null && rs.next()) {
				result = rs.getString("user_name");
			}
			if (cn != null) {
				cn.close();
			}

            
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	private Integer[] getUsers(int server_id) {
		ArrayList<Integer> result = new ArrayList<>();
		
		Connection cn = null;
		PreparedStatement  pstmt = null;
	    ResultSet rs = null;
	    String SQL = "select user_id from user_server_relationship where server_id = ?";
	    try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setInt(1, server_id);
			rs = pstmt.executeQuery();
			
			if(rs != null) {
				while(rs.next()) {
					System.out.println("getUsers: "+rs.getInt("user_id"));
					result.add(rs.getInt("user_id"));
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
