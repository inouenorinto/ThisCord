package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import db.mysql.MySqlManager;

public class VoiceChannelDAO {
	private static final String getVoiceChannels ="select * from voice_channel where server_id = ?";
	
	private static VoiceChannelDAO vcdao = null;
	static {
    	vcdao = new VoiceChannelDAO();
    }
	
	public static final VoiceChannelDAO getInstance() {
    	return vcdao;
    }
	
	public Map<Integer, String> getVoiceChannels(int serverId) {
		Map<Integer, String> result = new HashMap<>();
		
		try(	Connection cn = MySqlManager.getConnection();
				PreparedStatement ps = cn.prepareStatement(getVoiceChannels); ){
			
			ps.setInt(1,serverId);
			
			try(ResultSet rs = ps.executeQuery();){
				while(rs.next()) {
					result.put(rs.getInt("channel_id"), rs.getString("channel_name"));
				}
			}

			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
