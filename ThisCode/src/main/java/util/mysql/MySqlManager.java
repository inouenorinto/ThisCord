package util.mysql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlManager {
    private static String DATABASE_NAME = "thiscord";
    private static String PROPATIES = "?characterEncoding=UTF-8&useTimezone=true&serverTimezone=Asia/Tokyo";
    private static String URL = "jdbc:mySQL://localhost/" + DATABASE_NAME+PROPATIES;

    private static String USER = "testuser1";
    private static String PASS = "password";
		
    public static Connection getConnection() {

        Connection conn = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USER, PASS);
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;

    }
}
