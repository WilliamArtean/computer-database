package com.excilys.mantegazza.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class DataSource {
	
	private HikariConfig config;
    private HikariDataSource ds;

    public DataSource() {
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

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
	
}
