package commands;

import db.dao.MessageDataDAO;

public class TestCommand {
	public static void main(String[] args) {
		MessageDataDAO mddao = MessageDataDAO.getInstance();
		mddao.insertRecord(1, 1, "'2023/11/15'", "'aaaaa'");

	}
}