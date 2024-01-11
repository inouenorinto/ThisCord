package commands;

import db.dao.FriendRelationshipDAO;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class FriendRequestCommand extends AbstractCommand{

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		int userId = Integer.parseInt(req.getParameter("userId")[0]);
		int friendId = Integer.parseInt(req.getParameter("friendId")[0]);
		
		FriendRelationshipDAO fdao = FriendRelationshipDAO.getInstance();
		fdao.insertFriend(userId, friendId);
		
		System.out.println("FriendRequestCommand.java: ok");
	}
	
}
