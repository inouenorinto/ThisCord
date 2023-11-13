package commands;

import bean.RegisterUserDTO;
import db.dao.UserDataDAO;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;
import util.ImageSaver;

public class RegisterCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		String editedImageData = req.getParameter("editedImage")[0];
        String username = req.getParameter("username")[0];
        String password = req.getParameter("password")[0];
        String email = req.getParameter("email")[0];
        
        
        
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setEmail(email);
        dto.setUser_name(username);
        dto.setPassword(password);
        dto.setUser_icon(editedImageData);
        
        userRegister(dto);
        
	}
	
	private void userRegister(RegisterUserDTO dto) {
		UserDataDAO dao = new UserDataDAO();
		int flag = dao.insertUser(dto.getUser_name(), dto.getPassword(), dto.getEmail());
		
		if(flag != -1) {
			String path = "C:\\ThisLocal\\ThisCode\\src\\main\\webapp\\resource\\user_icons\\" + flag+".jpg";
	        ImageSaver.base64ToImage(dto.getUser_icon(), path);
			
			
			dao.updateIcon(dto.getEmail(), "resource\\user_icons\\"+flag+".jpg");
		}
		
	}

}
