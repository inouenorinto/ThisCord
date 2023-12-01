package commands;

import java.util.Random;

import bean.RegisterUserDTO;
import db.dao.UserDataDAO;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;
import util.ImageSaver;
import util.encrypt.Encryption;

public class RegisterCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		String editedImageData = req.getParameter("editedImage")[0];
		System.out.println("image: "+editedImageData);
        String username = req.getParameter("username")[0];
        String password = req.getParameter("password")[0];
        String email = req.getParameter("email")[0];
        
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setEmail(email);
        dto.setUser_name(username);
        dto.setPassword(password);
        dto.setUser_icon(editedImageData);
        if(dto.getUser_icon().substring(1) == null) {
        	System.out.println("null");
        } else {
        	System.out.println("not null");
        }
        userRegister(dto);
        res.setTarget("fn/login");
        
	}
	
	private void userRegister(RegisterUserDTO dto) {
		UserDataDAO dao = UserDataDAO.getInstance();
		int flag = dao.insertUser(dto.getUser_name(), dto.getPassword(), dto.getEmail());
		
		if(flag != -1) {
			String path = "C:\\ThisLocal\\ThisCode\\src\\main\\webapp\\resource\\user_icons\\" + flag+".jpg";
	        if(dto.getUser_icon().substring(1) != null) {
	        	System.out.println(dto.getUser_icon());
	        	ImageSaver.base64ToImage(dto.getUser_icon(), path);
	        	dao.updateIcon(dto.getEmail(), "resource\\user_icons\\"+flag+".jpg");
	        } else {
	        	Random random = new Random();
	            int num = random.nextInt(4) + 1;
	            dao.updateIcon(dto.getEmail(), "resource\\user_icons\\default"+num+".png");
	        }
		}
	}

}
