package commands;

import bean.UserBean;
import db.dao.UserDataDAO;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class LoginCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		String email = req.getParameter("email")[0];
		String password = req.getParameter("password")[0];
		
		UserDataDAO account = UserDataDAO.getInstance();
		UserBean userBean = account.getRecord(email);
		
		if(email.equals(userBean.getMailaddress()) && password.equals(userBean.getPassword())) {
			
        	req.setAttributeInSession("bean", userBean);
			res.setTarget("fn/chat");
		} else {
			res.setTarget("/login.html");
		}
		
	}

}
