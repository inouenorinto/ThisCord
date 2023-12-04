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
        
        String hashedPassword = Encryption.hash(password);	//パスワードのハッシュ化
        
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setEmail(email);
        dto.setUser_name(username);
        dto.setPassword(hashedPassword);
        dto.setUser_icon(editedImageData);
        if(dto.getUser_icon().substring(1) == null) {
        	System.out.println("null");
        } else {
        	System.out.println("not null");
        }
        System.out.println(dto.getUser_icon());
        userRegister(dto);
        res.setTarget("fn/login");
        
	}
	
	private void userRegister(RegisterUserDTO dto) {
		UserDataDAO dao = UserDataDAO.getInstance();
		int flag = dao.insertUser(dto.getUser_name(), dto.getPassword(), dto.getEmail());
		
		if(flag != -1) {
			String path = "C:\\ThisLocal\\ThisCode\\src\\main\\webapp\\resource\\user_icons\\" + flag+".jpg";
	        if(dto.getUser_icon().equals("image: default")) {
	        	ImageSaver.base64ToImage(dto.getUser_icon(), path);
	        	dao.updateIcon(dto.getEmail(), flag+".jpg");
	        } else {
	        	Random random = new Random();
	            int num = random.nextInt(4) + 1;
	            dao.updateIcon(dto.getEmail(), "default"+num+".png");
	        }
		}
	}

}
