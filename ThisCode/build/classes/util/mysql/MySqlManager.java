package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlManager {
    public static Connection getConnection() {
        //DB接続用定数
        String DATABASE_NAME = "thiscord";
        String PROPATIES = "?characterEncoding=UTF-8&useTimezone=true&serverTimezone=Asia/Tokyo";
        String URL = "jdbc:mySQL://localhost/" + DATABASE_NAME+PROPATIES;
        //DB接続用・ユーザ定数
        String USER = "testuser1";
        String PASS = "password";
        Connection conn = null;
        try {
            //MySQL に接続する
            Class.forName("com.mysql.cj.jdbc.Driver");
            //データベースに接続
            conn = DriverManager.getConnection(URL, USER, PASS);

            // データベースに対する処理
            System.out.println("データベースに接続に成功");
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
