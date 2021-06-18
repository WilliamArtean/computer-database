package com.excilys.mantegazza.cdb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@Configuration
@ComponentScan({ "com.excilys.mantegazza.cdb.service", "com.excilys.mantegazza.cdb.controller",
	"com.excilys.mantegazza.cdb.persistence", "com.excilys.mantegazza.cdb.dto.mappers",
	"com.excilys.mantegazza.cdb.persistence.mappers", "com.excilys.mantegazza.cdb.ui",
	"com.excilys.mantegazza.cdb.validator" })
public class AppConfig {

	@Bean
	public HikariDataSource getDataSource() {
		Properties props = new Properties();
		try {
			InputStream is = AppConfig.class.getClassLoader().getResourceAsStream("hikaridatasource.properties");
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HikariDataSource(new HikariConfig(props));
	}
	
	@Bean
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(getDataSource());
	}
	
}
