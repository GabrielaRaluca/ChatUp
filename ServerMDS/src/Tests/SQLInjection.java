package Tests;

import static org.junit.Assert.*;

import org.junit.Test;

import JDBC.JDBCConnection;

public class SQLInjection {

	@Test
	public void test() {
		JDBCConnection instance = JDBCConnection.getInstance();
		String query = "Drop table relatie";
		String result;
		result = instance.confirmFriendRequest(query, query);
		
		String username1 = "DianaCopaci";
		String username2 = "MihaiMohora";
		
		result = instance.confirmFriendRequest(username1, username2);
		assertEquals(!result.equals("Error"), true);
		
	}

}
