package commands;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import bean.UserBean;
import db.dao.ServerDataDAO;
import db.dao.TextChannelDataDAO;
import db.dao.UserDataDAO;
import db.dao.UserServerRelationshipDAO;
import db.dao.VoiceChannelDAO;
import framework.command.AbstractCommand;
import framework.context.RequestContext;
import framework.context.ResponseContext;

public class MakeServerCommand extends AbstractCommand {

	@Override
	public void execute(RequestContext req, ResponseContext res) {

		String server_name = req.getParameter("server_name")[0];
		String server_icon = req.getParameter("editedImage")[0];
		System.out.println("MakeServerCommand.java :\t\t"+server_name);
		int userid = Integer.parseInt(req.getParameter("userId")[0]);
		
		
		System.out.println("MakeServerCommand.java :\t\t"+ userid);
		
        ServerDataDAO serverDao = ServerDataDAO.getInstance();
        int next_id = serverDao.getMaxServerId() + 1;
        String path;
        
        if (server_icon != null && !server_icon.isEmpty()) {
            saveBase64Image(server_icon, next_id+".jpg");
            path = "resource/server_icons/" + next_id +".jpg";
        } else {
        	path = "default";
        }
        //server表に新しいサーバーを作る
        serverDao.insertNewServer(server_name, userid, path);
        int maxServerId = serverDao.getMaxServerId();
        
        //ホストユーザーをrelationship表に追加する
        UserServerRelationshipDAO serverRelationDao = UserServerRelationshipDAO.getInstance();
        serverRelationDao.addRelationship(maxServerId, userid);
		
        //デフォルトのテキストチャンネルを作る
        TextChannelDataDAO textChannelDao = TextChannelDataDAO.getInstance();
        textChannelDao.addTextChannel("一般", maxServerId);
        
        VoiceChannelDAO voiceDao = VoiceChannelDAO.getInstance();
        voiceDao.addVoiceChannel("一般", maxServerId);
        
		//res.setTarget("fn/chat");
		
		UserDataDAO account = UserDataDAO.getInstance();
		UserBean userBean = account.getRecord(userid);
		
		UserBean sessionUserBean = (UserBean)req.getAttributeInSession("bean"+userid);
		System.out.println("MakeServerCommand.java :\t\tユーザーID="+ userid);
		System.out.println("MakeServerCommand.java :\t\tbeanユーザーID="+ sessionUserBean.getUser_id());
		sessionUserBean = userBean;
		
		req.setAttributeInSession("bean"+userid, userBean);
		//res.setTarget("/chat.jsp");
		res.setRedirect("/chat.jsp?id="+userid);
	}
	
	private void saveBase64Image(String base64Data, String fileName) {
        String savePath = "C:/ThisLocal/ThisCode/src/main/webapp/resource/server_icons/" + fileName;
        try (OutputStream out = new FileOutputStream(savePath)) {
            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Data.split(",")[1]);
            out.write(imageBytes);
            System.out.println("せいこう");
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

}


