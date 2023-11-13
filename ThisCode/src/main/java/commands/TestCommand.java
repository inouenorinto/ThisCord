package commands;

import java.util.ArrayList;

import bean.ServerDataBean;
import db.dao.ServerDataDAO;

public class TestCommand {
	public static void main(String[] args) {
		ArrayList<ServerDataBean> serverdatas = null;
		ServerDataBean serverData = new ServerDataBean();
		ServerDataDAO serverdatadao = ServerDataDAO.getInstance();
		//UserDataDAO userdatadao = UserDataDAO.getInstance();
		//UserServerRelationshipDAO serverRelationdao =
				//UserServerRelationshipDAO.getInstance();
		
		serverdatas = serverdatadao.findAll();
		
		for (ServerDataBean bean : serverdatas) {
			System.out.println(bean.getServer_name());
		}
		
	}
}