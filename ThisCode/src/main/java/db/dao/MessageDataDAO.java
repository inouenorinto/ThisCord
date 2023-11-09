package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.MessageBean;
import util.mysql.MySqlManager;

public class MessageDataDAO{
    private static final String SELECT_MESSAGE_DATA = "SELECT * FROM message_data"
    		+ "WHERE server_id = ? && channel_id = ? ";
    private static final String UPDATE_MESSAGE = "UPDATE message_data SET message = ? "
            + "WHERE server_id = ? AND channel_id = ?";

    Connection cn;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public MessageDataDAO(Connection cn){
        this.cn = MySqlManager.getConnection();
    }

    public ArrayList<MessageBean> findRecord(int server_id, int channel_id) {
        ArrayList<MessageBean> result = new ArrayList<>();

        try {
            pstmt = cn.prepareStatement(SELECT_MESSAGE_DATA);
            pstmt.setInt(1, server_id);
            pstmt.setInt(2, channel_id);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                MessageBean messageBean = new MessageBean();
                messageBean.setUser_id(rs.getString("user_id"));
                messageBean.setSend_date(rs.getString("send_date"));
                messageBean.setMessage(rs.getString("message"));
                result.add(messageBean); 
            }

        } catch (SQLException e){
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


    public boolean updateRecord(int server_id, int channel_id, String newMessage) {
        boolean success = false;

        try {
            pstmt = cn.prepareStatement(UPDATE_MESSAGE);
            pstmt.setString(1, newMessage);
            pstmt.setInt(2, server_id);
            pstmt.setInt(3, channel_id);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }
}