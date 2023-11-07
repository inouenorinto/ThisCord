package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.ServerDataBean;
import util.mysql.MySqlManager;

public class ServerDataDAO {
    private static final String DB_SELECT = "SELECT * FROM server_data WHERE discordServerId = ";

    private Connection cn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    public ServerDataDAO(Connection cn) {
        this.cn = MySqlManager.getConnection();
    }

    public ArrayList<ServerDataBean> findAll(String discordServerId) {
        ArrayList<ServerDataBean> result = new ArrayList<>();
        try {
            pstmt = cn.prepareStatement(DB_SELECT);
            pstmt.setString(1, discordServerId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ServerDataBean serverDataBean = new ServerDataBean();
                serverDataBean.setDiscordServerId(rs.getString("discordServerId"));
                serverDataBean.setServerName(rs.getString("serverName"));
                serverDataBean.setHostId(rs.getString("hostId"));
                serverDataBean.setServerIcon(rs.getString("serverIcon"));
                serverDataBean.setServerMemberId(rs.getString("serverMemberId"));
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
}
