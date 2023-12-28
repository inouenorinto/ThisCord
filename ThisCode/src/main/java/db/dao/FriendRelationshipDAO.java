package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.mysql.MySqlManager;

public class FriendRelationshipDAO {
    private final String SELECT_FRIEND_LIST = "SELECT user_id1, user_id2 FROM friend_relationship WHERE user_id1 = ? OR user_id2 = ?";
    
	private static FriendRelationshipDAO dao = null;
	static{
		dao = new FriendRelationshipDAO();
	}

	public static FriendRelationshipDAO getInstance(){
		return dao;
	}

	private FriendRelationshipDAO(){}

	//フレンドリストを取得する
	public ArrayList<Integer> getFriendList(int userId){
		ArrayList<Integer> result = new ArrayList<>();

        try(	Connection cn = MySqlManager.getConnection();
				PreparedStatement ps = cn.prepareStatement(SELECT_FRIEND_LIST); ){
			
			ps.setInt(1, userId);
			ps.setInt(2, userId);
			
			try(ResultSet rs = ps.executeQuery();){
				while(rs.next()) {
					//user_id1からuser_id2どちらかの自分以外を取得する
					if(rs.getInt("user_id1") == userId) {
						result.add(rs.getInt("user_id2"));
					} else {
						result.add(rs.getInt("user_id1"));
					}
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
    }

}