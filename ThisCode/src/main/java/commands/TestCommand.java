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
			System.out.print("serverid: " + bean.getServer_id() + "\t");
			System.out.print("name: " + bean.getServer_name() + "\t");
			System.out.print("hostid: " + bean.getHost_id() + "\t");
			System.out.println("icon" + bean.getServer_icon());
		}

	}
}