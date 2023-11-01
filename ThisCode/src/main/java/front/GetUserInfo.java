package front;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;


@WebServlet("/get-user-info")
public class GetUserInfo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserBean bean = (UserBean) session.getAttribute("bean");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        System.out.println("gson bean"+ new Gson().toJson(bean));

        response.getWriter().write(new Gson().toJson(bean));
    }
}