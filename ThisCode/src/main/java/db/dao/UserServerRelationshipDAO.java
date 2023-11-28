package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.UserServerRelationshipBean;
import util.mysql.MySqlManager;

public class UserServerRelationshipDAO {
	private static final String SERVER_SELECT = ""; //わからんからあとで
	
	Connection cn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public UserServerRelationshipDAO(Connection cn) { 
		this.cn = MySqlManager.getConnection();
	}
	
	public ArrayList<UserServerRelationshipBean> findAll(int relationship_id) {
        ArrayList<UserServerRelationshipBean> result = new ArrayList<>();
        
        try{
            pstmt = cn.prepareStatement(SERVER_SELECT);
            pstmt.setInt(1,relationship_id);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                UserServerRelationshipBean userServerRelationshipBean = new UserServerRelationshipBean();
                userServerRelationshipBean.setRelationsip_id(rs.getInt("relationsip_id"));
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
}
