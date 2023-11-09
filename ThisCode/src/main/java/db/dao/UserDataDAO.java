package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.UserBean;
import bean.UserDataBean;
import util.mysql.MySqlManager;

public class UserDataDAO{
    private static final String DB_SELECT = "select * from user_data where discord_user_id = ?";

    private Connection cn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    
    private static UserDataDAO uddao = null;
    
    static {
    	uddao = new UserDataDAO();
    }
    
    public static final UserDataDAO getInstance() {
    	return uddao;
    }

    //Connectionとってくるコンストラクタ
    private UserDataDAO() {
        this.cn = MySqlManager.getConnection();
    }
    
    //インスタンスをとってくるクラスメソッドの呼び出し
    ServerDataDAO sddao = ServerDataDAO.getInstance();
    UserServerRelationshipDAO usrdao = UserServerRelationshipDAO.getInstance();

    //1行ずつ全部ArrayListにぶち込むメソッド
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
    
    //
	public UserBean getRecord(String email) {
	    String SQL="select * from user_data where mailaddress = ?";
	    
	    UserBean bean = new UserBean();
	    try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			
			if(rs != null && rs.next()) {
				
				bean.setUser_id(rs.getInt("user_id"));
				bean.setMailaddress(rs.getString("mailaddress"));
				bean.setPassword(rs.getString("password"));
				bean.setUser_name(rs.getString("user_name"));
				bean.setUser_icon(rs.getString("user_icon"));
				
				for(int num : usrdao.getServers(bean.getUser_id())) {
					System.out.println("getRecord: "+sddao.getServerName(num));
					bean.addRooms(num, sddao.getServerName(num));
				}
			}
			if (cn != null) {
				cn.close();
			}

            
		} catch(Exception e) {
			e.printStackTrace();
		}
	    return bean;
	}
	
	private boolean isLoginValid(String email, String password) {
	    String SQL = "select user_name, mailaddress, password from user_data where mailaddress = ?";
	    boolean flag = false;
		try {
			cn = MySqlManager.getConnection();
			pstmt = cn.prepareStatement(SQL);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			
			if(rs != null && rs.next()) {
				rs.getString("user_name");
				if(email.equals(rs.getString("mailaddress")) && password.equals(rs.getString("password"))) {
					flag =true;
				}
			}
			if (cn != null) {
				cn.close();
			}

            
		} catch(Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
