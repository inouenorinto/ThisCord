package endpoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.ImageSaver;

@WebServlet("/ImageUploadServlet")
@MultipartConfig
public class ImageUploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String editedImageData = request.getParameter("editedImage");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        
        System.out.println("username: "+ username);
        System.out.println("password: "+ password);
        System.out.println("email: "+ email);
        
        if (editedImageData != null && !editedImageData.isEmpty()) {
        	String savePath = "C:\\ThisLocal\\ThisCode\\src\\main\\webapp\\resource\\user_icons\\" + username+".jpg";
            ImageSaver.base64ToImage(editedImageData, savePath);
        } else {
        	System.out.println("nullです");
        }
    }    
}
