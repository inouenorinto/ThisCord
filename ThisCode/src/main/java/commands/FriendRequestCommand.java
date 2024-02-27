package commands;

import db.dao.FriendRelationshipDAO;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;
import util.Sanitizer;

public class FriendRequestCommand extends AbstractCommand{

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		int userId = Integer.parseInt(Sanitizer.sanitizing(req.getParameter("userId")[0]));
		int friendId = Integer.parseInt(Sanitizer.sanitizing(req.getParameter("friendId")[0]));
		
		FriendRelationshipDAO fdao = FriendRelationshipDAO.getInstance();
		fdao.insertFriend(userId, friendId);
		
		System.out.println("FriendRequestCommand.java: ok");
	}
	
}
