package commands;

import db.dao.MessageDataDAO;

public class TestCommand {
	public static void main(String[] args) {
		MessageDataDAO mddao = MessageDataDAO.getInstance();
		mddao.insertRecord(2, 2, "'2023/11/15'", "'aaaaa'");

	}
}