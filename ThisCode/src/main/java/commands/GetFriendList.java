package commands;

import com.google.gson.Gson;

import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class GetFriendList extends AbstractCommand{
	@Override
	public void execute(RequestContext req, ResponseContext res) {
		
		
		
		res.setCharacterEncoding("UTF-8");
		res.getWriter().write(new Gson().toJson(bean));
	}
}
