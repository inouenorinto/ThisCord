package commands;

import db.dao.TextChannelDataDAO;
import db.dao.VoiceChannelDAO;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class DeleteChannelCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {
		int channelId = Integer.parseInt(req.getParameter("channelId")[0]);
		String channelType = (String) req.getParameter("channelType")[0];
		
		if(channelType.equals("text")) {
			TextChannelDataDAO textDao = TextChannelDataDAO.getInstance();
			textDao.deleteTextChannel(channelId);
			
		} else {
			VoiceChannelDAO voiceDao = VoiceChannelDAO.getInstance();
			voiceDao.deleteVoiceChannel(channelId);
		}
		
		res.setStatus("ok");
	}

}
