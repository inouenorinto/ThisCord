package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.ServerDataBean;
import util.mysql.MySqlManager;

public class ServerDataDAO { //server表
    private static final String DB_SELECT = "SELECT * FROM server_data";
    private static final String DB_RECORD = "SELECT * FROM server_data WHERE server_id = ?";

    private Connection cn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    
    private static ServerDataDAO sddao = null;
    
    static {
    	sddao = new ServerDataDAO();
    }
    
    public static final ServerDataDAO getInstance() {
    	return sddao;
    }
    
    private ServerDataDAO() {
        this.cn = MySqlManager.getConnection();
    }

    public ArrayList<ServerDataBean> findAll() {
        ArrayList<ServerDataBean> result = new ArrayList<>();
        try {
            pstmt = cn.prepareStatement(DB_SELECT);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ServerDataBean serverDataBean = new ServerDataBean();
                serverDataBean.setServer_id(rs.getInt("server_id"));
                serverDataBean.setServer_name(rs.getString("server_name"));
                serverDataBean.setUser_id(rs.getInt("user_id"));
                serverDataBean.setServer_icon(rs.getString("server_icon"));
                result.add(serverDataBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    
    public ServerDataBean findRecord(int id) {
    	ServerDataBean serverDataBean = new ServerDataBean();
        try {
            pstmt = cn.prepareStatement(DB_RECORD);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {	
                serverDataBean.setServer_id(rs.getInt("server_id"));
                serverDataBean.setServer_name(rs.getString("server_name"));
                serverDataBean.setUser_id(rs.getInt("host_id"));
                serverDataBean.setServer_icon(rs.getString("server_icon"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return serverDataBean;
    }
    
    public String[] getServerNameAndIcon(int id) {
		Connection cn = null;
		PreparedStatement  pstmt = null;
	    ResultSet rs = null;
	    String result[] = new String[2];
	    String SQL = "select server_name, server_icon from server_data where server_id = ?";
		try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			
			if(rs != null && rs.next()) {
				result[0] = rs.getString("server_name");
				result[1] = rs.getString("server_icon");
			}
			if (cn != null) {
				cn.close();
			}

            
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
