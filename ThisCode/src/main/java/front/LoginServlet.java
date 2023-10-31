package front;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (isValidUser(username, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", null); 
            session.setAttribute("username", username);
            response.sendRedirect("/ThisCord/chat.html"); // チャット画面にリダイレクト
        } else {
            response.sendRedirect("/ThisCord/login.html"); // ログイン画面にリダイレクト
        }
    }

    boolean isValidUser(String username, String password) {
    	System.out.println(username+":"+password);
    	//return username.equals("admin") && password.equals("admin");
    	return true;
    }
}
