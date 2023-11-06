package util.mysql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlManager {
    public static Connection getConnection() {
        String DATABASE_NAME = "thiscord";
        String PROPATIES = "?characterEncoding=UTF-8&useTimezone=true&serverTimezone=Asia/Tokyo";
        String URL = "jdbc:mySQL://localhost/" + DATABASE_NAME+PROPATIES;

        String USER = "testuser1";
        String PASS = "password";
        Connection conn = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USER, PASS);

            System.out.println("データベースつながったよ");
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
