package commands;

import bean.UserBean;
import db.dao.UserDataDAO;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class ChatPageCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		UserBean sessionBean = (UserBean)req.getAttributeInSession("bean");
		
		UserDataDAO account = UserDataDAO.getInstance();
		UserBean userBean = account.getRecord(sessionBean.getMailaddress());
		
    	req.setAttributeInSession("bean", userBean);
		
		res.setRedirect("/chat.jsp");

	}
}
