package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.TextChannelDataBean;
import util.mysql.MySqlManager;

public  class TextChannelDataDAO {
	private static final String CHANNEL = "SELECT * FROM text_channel_data WHERE channel_id = ?";
	
	private Connection cn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private TextChannelDataDAO() {
		this.cn = MySqlManager.getConnection();
	}
	
	public ArrayList<TextChannelDataBean> findAll(int channel_id) {
		ArrayList<TextChannelDataBean> result = new ArrayList<>();

        try{
            pstmt = cn.prepareStatement(CHANNEL);
            pstmt.setInt(1,channel_id);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                TextChannelDataBean textChannelDataBean = new TextChannelDataBean();
                textChannelDataBean.setChannel_id(rs.getInt("channel_id"));
                textChannelDataBean.setChannel_name(rs.getString("channel_name"));
                textChannelDataBean.setServer_id(rs.getInt("server_id"));
                result.add(textChannelDataBean);   
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try{
                    pstmt.close();
                } catch (SQLException e){
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
