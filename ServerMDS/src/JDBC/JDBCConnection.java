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
	
	public String verifyUsername(String username)
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
			}
			finally{
				closeConnection();
			}
			
			return null;		
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
			}
			finally{
				closeConnection();
			}
			
			return null;		
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
		String query = "SELECT utilizator1, utilizator2 FROM relatie WHERE BINARY status = 1 AND (utilizator1 = ? OR utilizator2 = ?)";
		try {
			
			statement= connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, username);
			rs = statement.executeQuery();
			
			if(rs.next())
			{
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
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally{
			closeConnection();
		}
		return result;
	}
}
