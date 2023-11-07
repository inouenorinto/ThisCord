package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.UserDataBean;
import util.mysql.MySqlManager;

public class UserDataDAO{
    private static final String DB_SELECT = "select * from user_data where discord_user_id = ?";

    Connection cn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public UserDataDAO(Connection cn) {
        this.cn = MySqlManager.getConnection();
    }

    public ArrayList<UserDataBean> findAll(int User_Id) {
        ArrayList<UserDataBean> result = new ArrayList<>();

        try {
            pstmt = cn.prepareStatement(DB_SELECT);
            pstmt.setInt(1, User_Id);
            rs = pstmt.executeQuery();

            while(rs.next()) {
            	UserDataBean userDataBean = new UserDataBean();
            	userDataBean.setUser_id(rs.getInt("user_id"));
            	userDataBean.setMailaddress(rs.getString("mailaddress"));
            	userDataBean.setPassword(rs.getString("password"));
            	userDataBean.setUser_name(rs.getString("user_name"));
            	userDataBean.setUser_icon(rs.getString("user_icon"));
                result.add(userDataBean); 
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
