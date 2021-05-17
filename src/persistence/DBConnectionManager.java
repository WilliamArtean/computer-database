package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
	
	private String url = "jdbc:mysql://localhost:3306/computer-database-db";
	private String user = "admincdb";
	private String pswd = "qwerty1234";
	
	
	public String getUrl() {
		return url;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return pswd;
	}
	
	public Connection getNewConnection() throws SQLException {
		return DriverManager.getConnection(url, user, pswd);
	}
	
}
