package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.ServerDataBean;
import util.mysql.MySqlManager;

public class ServerDataDAO {
    private static final String DB_SELECT = "SELECT * FROM server_data";

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
                serverDataBean.setHost_id(rs.getInt("host_id"));
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
    
    public String getServerName(int id) {
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

}
