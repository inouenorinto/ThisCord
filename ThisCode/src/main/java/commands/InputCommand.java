package commands;

import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class InputCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		res.setTargetJsp("input");

	}

}
