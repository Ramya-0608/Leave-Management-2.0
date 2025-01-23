package database;

import java.sql.*;

public class DBConnect {
	private static final String URL = "jdbc:mysql://localhost:3306/leave";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "muthu@123";

	private static Connection connection;

	private DBConnect(){}
	
	public static Connection getConnection() throws SQLException{

		if(connection == null || connection.isClosed()) {
			try {
				connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
				System.out.println("Connected Successfull");
			}catch(SQLException e) {
				System.err.println("Connection Failed");
				throw e;
			}
		}
		
		return connection;
	}
	
	public static void closeConnection() {
		if(connection!=null) {
			try {
				connection.close();
				System.out.println("Connection Closed");
			}catch(SQLException e) {
				System.err.println("Failed to close Connection");
			}
		}
	}

}
