package com.excilys.mantegazza.cdb.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
	
	private String url = "jdbc:mysql://localhost:3306/computer-database-db";
	private String user = "admincdb";
	private String pswd = "qwerty1234";
	
	/**
	 * Get a new connection to the database. The DBConnectionManager will not close it automatically.
	 * @return A Connection to the database.
	 * @throws SQLException
	 */
	public Connection getNewConnection() throws SQLException {
		return DriverManager.getConnection(url, user, pswd);
	}
	
}
