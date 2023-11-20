package commands;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import bean.UserBean;
import db.dao.ServerDataDAO;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;
import util.mysql.MySqlManager;

public class MakeServerCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {

		String server_name = req.getParameter("server_name")[0];
		String server_icon = req.getParameter("editedImage")[0];
		System.out.println("server_name: "+server_name);
		UserBean bean = (UserBean)req.getAttributeInSession("bean");
		System.out.println("userid: "+bean.getUser_id());
		
        ServerDataDAO serverDao = ServerDataDAO.getInstance();
        int next_id = serverDao.getMaxServerId() + 1;
		
        if (server_icon != null && !server_icon.isEmpty()) {
            saveBase64Image(server_icon, next_id+".jpg");
        } else {
        	System.out.println("nullです");
        }
        

        String path = "resource/server_icons/" + next_id +".jpg";
        
        
		int server_id = makeServer(server_name,  bean.getUser_id(), path);
		
		res.setTarget("fn/chat");
	}
	
	private void saveBase64Image(String base64Data, String fileName) {
        String savePath = "C:\\ThisLocal\\ThisCode\\src\\main\\webapp\\resource\\server_icons\\" + fileName;
        try (OutputStream out = new FileOutputStream(savePath)) {
            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Data.split(",")[1]);
            out.write(imageBytes);
            System.out.println("せいこう");
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
	
	private int makeServer(String server_name, int user_id,String path) {
		int flag = -1;
		
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
			pstmt.setString(3, path);
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
			
			flag = index;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
}


