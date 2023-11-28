package util.mysql;


import java.sql.Connection;

public class MySqlManager {
    public static Connection getConnection() {
//        String DATABASE_NAME = "thiscord";
//        String PROPATIES = "?characterEncoding=UTF-8&useTimezone=true&serverTimezone=Asia/Tokyo";
//        String URL = "jdbc:mySQL://localhost/" + DATABASE_NAME+PROPATIES;
//    	Connection cn = null;
//    	PrintWriter out = null;
//    	
//    	try {
//            String uri = "jdbc:mysql://mydatabase.ccsxuhcaf1js.ap-northeast-1.rds.amazonaws.com:3306/thiscord";
//            Class.forName("com.mysql.jdbc.Driver");
//            cn = DriverManager.getConnection(uri,"testuser1","password");
//            System.out.println("Connection Success!");
//            PreparedStatement ps = (PreparedStatement)cn.prepareStatement("SELECT * FROM user_data");
//            ResultSet rs = ps.executeQuery();
//            System.out.println("SELECT!");
//            while(rs.next()) {
//                   String number = rs.getString("mailaddress");
//                   out.print(number);
//            }
//          }catch(Exception e) {
//        	  		System.out.println("Connection fail!");
//                    out.println(e.toString());
//            }
    	Connection cn = null;
    	
        return cn;
    }
}
