package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUp {

	private static ResultSet rs;
	private static Connection connection;
	private static PreparedStatement statement;
	private String query;
	
	public SignUp()
	{
		rs = null;
		connection = null;
		statement = null;
		query = null;
	}
	
	public static String verifyUsername(String username)
	{
		String result = null;
		String query = "SELECT username FROM utilizatori WHERE username = ?";
		
		
			try {
				connection = JDBCConnection.getConnection();
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
				if(connection != null)
				{
					try{
						connection.close();
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			return null;		
	}
	
	public static String verifyEmail(String email)
	{
		String result = null;
		String query = "SELECT email FROM utilizatori WHERE email = ?";
		
		
			try {
				connection = JDBCConnection.getConnection();
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
				if(connection != null)
				{
					try{
						connection.close();
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			return null;		
	}
	
	public static String verify(String username, String email, String password)
	{
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
		String query = "INSERT INTO utilizatori (username, email, password) VALUES(?,?,?)";
		
		
			try {
				connection = JDBCConnection.getConnection();
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
				if(connection != null)
				{
					try{
						connection.close();
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			return null;	
	}
}
