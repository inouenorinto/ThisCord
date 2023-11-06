package commands;

import com.google.gson.Gson;

import bean.UserBean;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class GetUaserInfoCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		UserBean bean = (UserBean)req.getAttributeInSession("bean");
		res.setContentType("application/json");
		System.out.println("kokomade");
		System.out.println("gson bean"+ new Gson().toJson(bean));
		res.setCharacterEncoding("UTF-8");
		res.getWriter().write(new Gson().toJson(bean));
	}
}
