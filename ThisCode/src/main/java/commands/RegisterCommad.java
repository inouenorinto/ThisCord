package commands;

import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class RegisterCommad extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		// TODO 自動生成されたメソッド・スタブ
		String editedImageData = req.getParameter("editedImage")[0];
        String username = req.getParameter("username")[0];
        String password = req.getParameter("password")[0];
        String email = req.getParameter("email")[0];
        
        userRegister(editedImageData, username, password, email);
        
	}
	
	private void userRegister(String user_icon, String user_name, String password, String email) {
		
	}

}
