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
		//int userid = Integer.parseInt(req.getParameter("userId")[0]);
		
		UserDataDAO account = UserDataDAO.getInstance();
		UserBean userBean = account.getRecord(sessionBean.getUser_id());
		//UserBean userBean = account.getRecord(userid);
		
    	req.setAttributeInSession("bean", userBean);
		
		res.setRedirect("/chat.jsp");

	}
}
