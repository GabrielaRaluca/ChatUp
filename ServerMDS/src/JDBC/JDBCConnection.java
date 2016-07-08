package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class JDBCConnection {

	//static reference to itself
	private static JDBCConnection instance; 
	private static final String URL = 
			"jdbc:mysql://localhost/mds?useSSL=false";
	private static final String USER = "root";
	private static final String PASSWORD = "parola";
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 
	Connection connection;
	//private constructor
	
	
	public static JDBCConnection getInstance()
	{
		instance = new JDBCConnection();
		return instance;
	}
	
	private JDBCConnection()
	{
		
	}
	
	private void initialize()
	{
		try {
			Class.forName(DRIVER_CLASS);
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

	public String Authentication(String username, String password)
	{
		initialize();
		PreparedStatement statement;
		ResultSet rs;
		String result = null;
		String query = "SELECT password FROM utilizatori WHERE BINARY username = ?";
		boolean isNull = false;
		
		
			try
			{
				statement= connection.prepareStatement(query);
				statement.setString(1, username);
				rs = statement.executeQuery();
				
				if(!rs.next())
					{
						isNull = true;
					}
				else
				{
					result = rs.getString("password");
				}
			}catch(SQLException e)
			{
				e.printStackTrace();
				return "Error";
			}
			finally{
				closeConnection();
			}
			if(isNull)
			{
				return "Wrong username, try again!";
			}
			else if(!result.equals(password))
			{
				return "Wrong password, try again!";
			}
		
		return null;
	}
	
	public String verifyUsername(String username)//returns null if username doesnt exist in the db, != null if the uername exists 
	{
		initialize();
		PreparedStatement statement;
		ResultSet rs;
		String query = "SELECT username FROM utilizatori WHERE BINARY username = ?";
		
		
			try {
				
				statement= connection.prepareStatement(query);
				statement.setString(1, username);
				rs = statement.executeQuery();
				
				if(!rs.next())
					{
						return null;
					}
				else
				{
					return "Invalid username";
				}
			}catch(SQLException e)
			{
				e.printStackTrace();
				return "Error";
			}
			finally{
				closeConnection();
			}
			
			//return null;		
	}
	
	public String verifyEmail(String email)
	{
		initialize();
		PreparedStatement statement;
		ResultSet rs;
		String query = "SELECT email FROM utilizatori WHERE BINARY email = ?";
		
		
			try {
				statement= connection.prepareStatement(query);
				statement.setString(1, email);
				rs = statement.executeQuery();
				
				if(!rs.next())
					{
						return null;
					}
				else
				{
					return "Email already taken";
				}
			}catch(SQLException e)
			{
				e.printStackTrace();
				return "Error";
			}
			finally{
				closeConnection();
			}
			
			//return null;		
	}
	
	public String verify(String username, String email, String password)
	{
		
		PreparedStatement statement;
		
		String result = verifyUsername(username);
		
		if(result != null)
		{
			return result;
		}
		result = verifyEmail(email);
		if(result != null)
		{
			return result;
		}
		initialize();
		String query = "INSERT INTO utilizatori (username, email, password) VALUES(?,?,?)";
		
		
			try {
				
				statement= connection.prepareStatement(query);
				statement.setString(1, username);
				statement.setString(2, email);
				statement.setString(3, password);
				statement.executeUpdate();
			
			}catch(SQLException e)
			{
				e.printStackTrace();
				return "Error";
			}
			finally{
				closeConnection();
			}
			
			return null;	
	}
	
	public ArrayList<String> getFriends(String username)
	{
		initialize();
		PreparedStatement statement;
		ResultSet rs;
		ArrayList<String> result = new ArrayList<String>();
		String query = "SELECT utilizator1, utilizator2 FROM relatie WHERE  status = 1 AND (BINARY utilizator1 = ? OR BINARY utilizator2 = ?)";
		try {
			
			statement= connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, username);
			rs = statement.executeQuery();
			if(!rs.next())
				return result;
			
				do
				{
					String friend1 = rs.getString("utilizator1");
					String friend2 = rs.getString("utilizator2");
					if(friend1.equals(username))
					{
						result.add(friend2);
					}
					if(friend2.equals(username))
					{
						result.add(friend1);
					}
				}
				while(rs.next());
			
		}catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		finally{
			closeConnection();
		}
		return result;
	}
	
	public ArrayList<String> getBlockedFriends(String username)
	{
		initialize();
		PreparedStatement statement;
		ResultSet rs;
		ArrayList<String> result = new ArrayList<String>();
		String query = "SELECT utilizator1, utilizator2 FROM relatie WHERE  status = 2 AND BINARY action = ?";
		try {
			
			statement= connection.prepareStatement(query);
			statement.setString(1, username);
			rs = statement.executeQuery();
			if (!rs.next())
				return result;

			do {
				String friend1 = rs.getString("utilizator1");
				String friend2 = rs.getString("utilizator2");
				if (friend1.equals(username)) {
					result.add(friend2);
				}
				if (friend2.equals(username)) {
					result.add(friend1);
				}
			} while (rs.next());

		}catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		finally{
			closeConnection();
		}
		return result;
	}
	
	public String verifyFriendship(String username1, String username2)
	{
		initialize();
		PreparedStatement statement;
		ResultSet rs;
		String query = "SELECT utilizator1 from relatie WHERE (BINARY utilizator1 = ? AND binary utilizator2 = ?) OR (BINARY utilizator2 = ? AND utilizator1 = ?)";
		
		
		
			try
			{
				statement= connection.prepareStatement(query);
				statement.setString(1, username1);
				statement.setString(2, username2);
				statement.setString(3, username1);
				statement.setString(4, username2);
				rs = statement.executeQuery();
				
				if(!rs.next()) //if not already friends 
					{
						return null;
					}
				else
				{
					return "You're already friends!";
				}
			}catch(SQLException e)
			{
				e.printStackTrace();
				return "Error";
			}
			finally{
				closeConnection();
			}
		
		//return null;
	}
	public String createNewFriendship(String username1, String username2) //returns null if it has inserted a new row, and a string if it didnt
	{
		PreparedStatement statement;
		//username1 = the one who sent the request
		//username2 = the one who received the request
		String result = verifyUsername(username2); //verify the username of the new friend
		
		if(result == null)
		{
			return "Username does not exist in the database";
		}
		
		result = verifyFriendship(username1, username2);
		if(result != null)
		{
			return result; //if users are already friends
		}
		
		initialize();
		String query = "INSERT INTO relatie VALUES(?,?, ?, ?)";
		
		
			try {
				
				statement= connection.prepareStatement(query);
				statement.setString(1, username1);
				statement.setString(2, username2);
				statement.setInt(3, 0);
				statement.setString(4, username1);
				statement.executeUpdate();
			
			}catch(SQLException e)
			{
				e.printStackTrace();
				return "Error";
			}
			finally{
				closeConnection();
			}
			
		return null;
	}
	
	public ArrayList<String> getPendingRequests(String username)
	{
		ArrayList<String> result = new ArrayList<String>();
		initialize();
		PreparedStatement statement;
		ResultSet rs;
		String query = "SELECT action from relatie WHERE binary utilizator2 = ? AND status = ? AND action != ?";
		String element;
		
		
			try
			{
				statement= connection.prepareStatement(query);
				statement.setString(1, username);
				
				statement.setInt(2, 0);
				statement.setString(3, username);
				rs = statement.executeQuery();
				if(!rs.next())
					return result;
				do
				{
					element = rs.getString("action");
					result.add(element);
				}
				while(rs.next());
			}catch(SQLException e)
			{
				e.printStackTrace();
				return null;
			}
			finally{
				closeConnection();
			}
		return result;
	}
	
	//primim username1-ul unui client si username2-ul celui care i-a trimis cererea de prietenie
	//(va avea codul username = username2)
	public String confirmFriendRequest(String username1, String username2)
	{
		PreparedStatement statement;
		initialize();
		String query = "UPDATE relatie SET status = 1 WHERE binary utilizator1 = ? AND binary utilizator2 = ? AND status = ?";
		
		
			try {
				
				statement= connection.prepareStatement(query);
				statement.setString(2, username1);
				statement.setString(1, username2);
				statement.setInt(3, 0);
				statement.executeUpdate();
			
			}catch(SQLException e)
			{
				e.printStackTrace();
				return "Error";
			}
			finally{
				closeConnection();
			}
			return null;
		
	}
	
	public String declineFriendRequest(String username1, String username2)
	{
		PreparedStatement statement;
		initialize();
		String query = "DELETE from relatie WHERE utilizator1 = ? AND utilizator2 = ? AND status = ?";
		
		
			try {
				
				
				statement= connection.prepareStatement(query);
				statement.setString(2, username1);
				statement.setString(1, username2);
				statement.setInt(3, 0);
				statement.executeUpdate();
			
			}catch(SQLException e)
			{
				e.printStackTrace();
				return "Error";
			}
			finally{
				closeConnection();
			}
			return null;
	}
	
	public String blockFriend(String username1, String username2)
	{
		PreparedStatement statement;
		//username1 = the one who blocked username2
		//username2 = the one who was blocked by username1
		String result = verifyUsername(username2); //verify the username of the new friend
				
		if(result == null)
		{
			return "Username does not exist in the database";
		}
				
		result = verifyFriendship(username1, username2);
		if(result == null)
		{
			return "You are not friends!"; //if users are already friends
		}
		initialize();
		String query = "UPDATE relatie SET status = 2, action = ? WHERE (utilizator1 = ? AND utilizator2 = ?) OR (utilizator1 = ? AND utilizator2 = ?)";
		
		
		try {
			
			statement= connection.prepareStatement(query);
			statement.setString(1, username1);
			statement.setString(2, username1);
			statement.setString(3, username2);
			statement.setString(4, username2);
			statement.setString(5, username1);
			statement.executeUpdate();
		
		}catch(SQLException e)
		{
			e.printStackTrace();
			return "Error";
		}
		finally{
			closeConnection();
		}
		return null;
	}
	public String insertMessages(String username1, String username2, String message)
	{
		initialize();
		PreparedStatement statement;
		String query = "INSERT INTO conversatie VALUES(sysdate(6),?, ?, ?)";
		
		
		try {
			
			statement= connection.prepareStatement(query);
			statement.setString(1, username1);
			statement.setString(2, username2);
			statement.setString(3, message);
			statement.executeUpdate();
		
		}catch(SQLException e)
		{
			e.printStackTrace();
			return "Error";
		}
		finally{
			closeConnection();
		}
		
		return null;
	}
	
	public ArrayList<String> getMessages(String username1, String username2)
	{/*AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA*/
		ArrayList<String> messages = new ArrayList<String>();
		PreparedStatement statement;
		ResultSet rs;
		initialize();
		String query = "SELECT utilizator1, date_format(date, '%Y-%M-%D %H:%I'), mesaj FROM conversatie WHERE (utilizator1 = ? AND utilizator2 = ?)  OR (utilizator2 = ? AND utilizator1 = ?) LIMIT 50";
		try {
			
			statement= connection.prepareStatement(query);
			statement.setString(1, username1);
			statement.setString(2, username2);
			statement.setString(3, username1);
			statement.setString(4, username2);
			rs = statement.executeQuery();
			if(!rs.next())
				return messages;
			
				do
				{
					String username = rs.getString("utilizator1");
					String mesaj = rs.getString("mesaj");
					String message = username + ": " + mesaj;
					messages.add(message);
					
				}
				while(rs.next());
			
		}catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		finally{
			closeConnection();
		}
		return messages;
	}
	
	String unblockFriend(String username1, String username2)
	{
		PreparedStatement statement;
		initialize();
		String query = "DELETE from relatie WHERE ((utilizator1 = ? AND utilizator2 = ?) OR (utilizator2 = ? AND utilizator1 = ?)) AND status = 2";
		
		
			try {
				
				
				statement= connection.prepareStatement(query);
				statement.setString(1, username1);
				statement.setString(2, username2);
				statement.setString(3, username1);
				statement.setString(4, username2);
				statement.executeUpdate();
			
			}catch(SQLException e)
			{
				e.printStackTrace();
				return "Error";
			}
			finally{
				closeConnection();
			}
			return null;
		
	}
	
	
}
