package front;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mysql.MySqlManager;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (isValidUser(username, password)) {
        	UserBean bean = new UserBean();
        	bean.setUsername(username);
        	bean.setPassword(password);
        	bean.addRooms("room1");
        	bean.addRooms("room2");
        	MySqlManager.getConnection();
        	
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("bean", bean);
            response.sendRedirect("/ThisCord/chat.html");
        } else {
            response.sendRedirect("/login.html");
        }
    }

    boolean isValidUser(String username, String password) {
    	System.out.println(username+":"+password);
    	//return username.equals("admin") && password.equals("admin");
    	return true;
    }
}

