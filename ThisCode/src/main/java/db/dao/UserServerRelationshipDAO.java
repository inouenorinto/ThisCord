//us_relationship表
package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.UserServerRelationshipBean;
import util.mysql.MySqlManager;

public class UserServerRelationshipDAO {
	private static final String SERVER_SELECT = "select * from user_server_relationship"; //これでいいかわからん
	private static final String FINDRECORD = "select * from user_server_relationship where user_id = ? and server_id = ?";

	
	Connection cn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	private static UserServerRelationshipDAO usrdao = null;
    
    static {
    	usrdao = new UserServerRelationshipDAO();
    }
    
    public static final UserServerRelationshipDAO getInstance() {
    	return usrdao;
    }
	
	private UserServerRelationshipDAO() { 
		this.cn = MySqlManager.getConnection();
	}
	
	public ArrayList<UserServerRelationshipBean> findAll() {
        ArrayList<UserServerRelationshipBean> result = new ArrayList<>();
        
        try{
            pstmt = cn.prepareStatement(SERVER_SELECT);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                UserServerRelationshipBean userServerRelationshipBean = new UserServerRelationshipBean();
                userServerRelationshipBean.setUser_id(rs.getInt("user_id"));
                userServerRelationshipBean.setServer_id(rs.getInt("server_id"));

                result.add(userServerRelationshipBean);
            }
        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            if(pstmt != null) {
                try{
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (cn != null) {
                try{
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
	
	
	
	public UserServerRelationshipBean findRecord(int id) {
        UserServerRelationshipBean userServerRelationshipBean = new UserServerRelationshipBean();
        
        try{
            pstmt = cn.prepareStatement(FINDRECORD);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                userServerRelationshipBean.setUser_id(rs.getInt("user_id"));
                userServerRelationshipBean.setServer_id(rs.getInt("server_id"));

            }
        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            if(pstmt != null) {
                try{
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (cn != null) {
                try{
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return userServerRelationshipBean;
    }

	
	
	public Integer[] getServers(int user_id) {
		ArrayList<Integer> result = new ArrayList<>();

	    String SQL = "select server_id from user_server_relationship where user_id = ?";
	    try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery();
			
			if(rs != null) {
				while(rs.next()) {
					System.out.println("getServers: "+rs.getInt("server_id"));
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
