package JDBC;

import java.util.ArrayList;

public class JDBCHandlers {

	private final static JDBCConnection instance = JDBCConnection.getInstance();
	
	public static String authentication(String username, String password)
	{
		return instance.Authentication(username, password);
	}
	
	public static String signUp(String username, String email, String password)
	{
		return instance.verify(username, email, password);
	}
	public static ArrayList<String> getFriends(String username)
	{
		return instance.getFriends(username);
	}
}
