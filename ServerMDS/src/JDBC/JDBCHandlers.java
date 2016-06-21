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
	
	public static String addFriend(String username1, String username2)
	{
		return instance.createNewFriendship(username1, username2);
	}
	
	public static ArrayList<String> getPendingRequests(String username)
	{
		return instance.getPendingRequests(username);
	}
	public static ArrayList<String> getBlockedFriends(String username)
	{
		return instance.getBlockedFriends(username);
	}
	public static void confirmFriendRequest(String username1, String username2)
	{
		instance.confirmFriendRequest(username1, username2);
	}
	
	public static void declineFriendRequest(String username1, String username2)
	{
		instance.declineFriendRequest(username1, username2);
	}
	public static String blockFriend(String username1, String username2)
	{
		return instance.blockFriend(username1, username2);
	}
	public static void insertMessages(String username1, String username2, String message)
	{
		instance.insertMessages(username1, username2, message);
	}
	public static ArrayList<String> getMessages(String username1, String username2)
	{
		return instance.getMessages(username1, username2);
	}
	public static void unblockFriend(String username1, String username2)
	{
		instance.unblockFriend(username1, username2);
	}
}

