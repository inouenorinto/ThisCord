package commands;

import db.dao.MessageDataDAO;

public class TestCommand {
	public static void main(String[] args) {
		ArrayList<ServerDataBean> serverdatas = null;
		ServerDataDAO serverdatadao = ServerDataDAO.getInstance();
		//UserDataDAO userdatadao = UserDataDAO.getInstance();
		//UserServerRelationshipDAO serverRelationdao =
				//UserServerRelationshipDAO.getInstance();
		
//		serverdatas = serverdatadao.findAll();
		
//		for (ServerDataBean bean : serverdatas) {
//			System.out.print("serverid: " + bean.getServer_id() + "\t");
//			System.out.print("name: " + bean.getServer_name() + "\t");
//			System.out.print("hostid: " + bean.getUser_id() + "\t");
//			System.out.println("icon" + bean.getServer_icon());
//		}
		
		ServerDataBean sdbean = serverdatadao.findRecord(1);
		
		System.out.print("serverid:" + sdbean.getServer_id() + "\t");
		System.out.print("name:" + sdbean.getServer_name() + "\t");
		System.out.print("hostid:" + sdbean.getUser_id() + "\t");
		System.out.println("icon:"+ sdbean.getServer_icon());
		
		String[] serverStr = serverdatadao.getServerNameAndIcon(1);
		
		for (String str : serverStr) {
			System.out.println(str);
		}

	}
}