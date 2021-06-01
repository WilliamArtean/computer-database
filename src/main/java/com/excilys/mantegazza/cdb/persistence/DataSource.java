package com.excilys.mantegazza.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {
	
	private static HikariConfig config;
    private static HikariDataSource ds;

    
    static {
    	try {
    		Properties props = new Properties();
    		InputStream is = DataSource.class.getClassLoader().getResourceAsStream("hikaridatasource.properties");
			props.load(is);
			config = new HikariConfig(props);
			ds = new HikariDataSource(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private DataSource() { }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
	
}
