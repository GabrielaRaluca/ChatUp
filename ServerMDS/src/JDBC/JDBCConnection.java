package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

	//static reference to itself
	private static JDBCConnection instance = new JDBCConnection();
	public static final String URL = 
			"jdbc:mysql://localhost/mds?useSSL=false";
	public static final String USER = "root";
	public static final String PASSWORD = "parola";
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 
	//private constructor
	
	private JDBCConnection() {
		try {
			//Step 2: Load MySQL Java driver
				Class.forName(DRIVER_CLASS).newInstance();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch(InstantiationException e)
		{
			e.printStackTrace();
		}
		}
	private Connection createConnection() 
	{
		Connection connection = null;
		try{
			
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		}
		catch(SQLException e)
		{
			System.out.println("ERROR: Unable to Connect to Database");
		}
		return connection;
	}

	public static Connection getConnection() 
	{
		return instance.createConnection();
	}
}
