package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import bean.UserBean;
import bean.UserDataBean;
import db.mysql.MySqlManager;

//account表
public class UserDataDAO{
    private static final String DB_SELECT = "select * from account where user_id = ?";

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
    
    public UserDataBean getUserInfo(int user_id) {
    	UserDataBean userDataBean = new UserDataBean();
    	
    	try {
    		this.cn = MySqlManager.getConnection();
            pstmt = cn.prepareStatement(DB_SELECT);
            pstmt.setInt(1, user_id);
            rs = pstmt.executeQuery();

            if(rs.next()) {
            	userDataBean.setUser_id(rs.getInt("user_id"));
            	userDataBean.setMailaddress(rs.getString("mailaddress"));
            	userDataBean.setPassword(rs.getString("password"));
            	userDataBean.setUser_name(rs.getString("user_name"));
            	userDataBean.setUser_icon(rs.getString("user_icon"));
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
        }
        return userDataBean;
    }
    
    //
	public UserBean getRecord(String email) {
	    String SQL="select * from account where mailaddress = ?";
	    
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
					
					String info[] = sddao.getServerNameAndIcon(num);
					bean.addRooms(num, info[0], info[1]);
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
	    String SQL = "select user_name, mailaddress, password from account where mailaddress = ?";
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
	
	public int insertUser(String user_name, String password, String email) {
		String insert = "INSERT INTO account (mailaddress, password, user_name ) VALUES (?, ?, ?)";
		String select = "select mailaddress from account";
		String select_id ="select user_id from account where mailaddress=?"; 
		int flag = -1;
		Statement st = null;
		try {
			
			st = cn.createStatement();
			rs = st.executeQuery(select);
			
			if(rs.next()) {
				rs = null;
				pstmt = cn.prepareStatement(insert);
				pstmt.setString(1, email);
				pstmt.setString(2, password);
				pstmt.setString(3, user_name);
				int fla = pstmt.executeUpdate();
				
				if(fla != -1) {
					pstmt = cn.prepareStatement(select_id);
					pstmt.setString(1, email);
					rs = pstmt.executeQuery();
					if(rs.next()) {
						System.out.println("userid"+ rs.getInt("user_id"));
						flag = rs.getInt("user_id");
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return flag;
	}
	
	public int updateIcon(String email, String path) {
		String update = "update account set user_icon=? where mailaddress=?";
	    PreparedStatement ps = null;	    
	    
	    int flag = -1;
	    
		try {
			ps = cn.prepareStatement(update);
			ps.setString(1, path);
			ps.setString(2, email);
			flag = ps.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return flag;
	}
	
	public void close() {
		try {
			if (cn != null) {
				cn.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}
	
}
