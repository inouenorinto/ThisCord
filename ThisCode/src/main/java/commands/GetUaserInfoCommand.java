package commands;

import com.google.gson.Gson;

import bean.UserBean;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class GetUaserInfoCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		int userId = Integer.parseInt(req.getParameter("id")[0]);
		UserBean bean = (UserBean)req.getAttributeInSession("bean"+userId);
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		res.getWriter().write(new Gson().toJson(bean));
	}
}
