package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Authentication {

	private static ResultSet rs;
	private static Connection connection;
	private static PreparedStatement statement;
	private String query;
	
	public Authentication()
	{
		rs = null;
		connection = null;
		statement = null;
		query = null;
	}
	
	public static String verify(String username, String password)
	{
		String result = null;
		String query = "SELECT password FROM utilizatori WHERE username = ?";
		boolean isNull = false;
		
		
			try {
				connection = JDBCConnection.getConnection();
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
}
