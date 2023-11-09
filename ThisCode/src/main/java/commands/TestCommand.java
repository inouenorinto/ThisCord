package commands;

import java.util.ArrayList;
import java.util.Iterator;

import bean.ServerDataBean;
import db.dao.ServerDataDAO;

public class TestCommand {
	public static void main(String[] args) {
		ArrayList serverdatas = null;
		ServerDataBean serverData = new ServerDataBean();
		ServerDataDAO serverdatadao = ServerDataDAO.getInstance();
		//UserDataDAO userdatadao = UserDataDAO.getInstance();
		//UserServerRelationshipDAO serverRelationdao =
				//UserServerRelationshipDAO.getInstance();
		
		serverdatas = serverdatadao.findAll(1);
		
		Iterator ite = serverdatas.iterator();
		while(ite.hasNext()) {
			System.out.println("Discord Server ID: " + serverData.getServer_id());
            System.out.println("Server Name: " + serverData.getServer_name());
            System.out.println("Host ID: " + serverData.getHost_id());
            System.out.println("Server Icon: " + serverData.getServer_icon());
            System.out.println("Server Member ID: " + serverData.getServer_member_id());
		}
	}
}