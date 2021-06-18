package com.excilys.mantegazza.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionManager {
	
	private String url;
	private String user;
	private String password;
	private String driverClass;
	private Logger logger = LoggerFactory.getLogger(DBConnectionManager.class);
	
	private DBConnectionManager() {
		try {
			InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties");
			Properties properties = new Properties();
			properties.load(input);
			
			url = properties.getProperty("url");
			user = properties.getProperty("user");
			password = properties.getProperty("password");
			driverClass = properties.getProperty("driver");
			Class.forName(driverClass);
		} catch (IOException e) {
			logger.error("Unable to load database properties file");
		} catch (ClassNotFoundException e) {
			logger.error("Unable to load Driver class");
		}
	}
	
	private static class SingletonHolder {
		private static final DBConnectionManager DBMANAGER_INSTANCE = new DBConnectionManager();
	}
	
	public static DBConnectionManager getInstance() {
		return SingletonHolder.DBMANAGER_INSTANCE;
	}
	
	/**
	 * Get a new connection to the database. The DBConnectionManager will not close it automatically.
	 * @return A Connection to the database.
	 * @throws SQLException
	 */
	public Connection getNewConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
	
}
