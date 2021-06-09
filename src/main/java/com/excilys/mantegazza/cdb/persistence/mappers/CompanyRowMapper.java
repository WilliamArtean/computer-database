package com.excilys.mantegazza.cdb.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Company.CompanyBuilder;

@Component
public class CompanyRowMapper implements RowMapper<Company> {

	@Override
	public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
		CompanyBuilder companyBuilder = new Company.CompanyBuilder();
		companyBuilder.withID(rs.getLong("id"));
		companyBuilder.withName(rs.getString("name"));
		return companyBuilder.build();
	}

}
