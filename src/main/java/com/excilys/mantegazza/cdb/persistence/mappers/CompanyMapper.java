package com.excilys.mantegazza.cdb.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.excilys.mantegazza.cdb.model.Company;

@Component
public class CompanyMapper {

	public Optional<Company> mapToCompany(ResultSet rs) throws SQLException {
		if (!rs.isBeforeFirst()) {
			return Optional.empty();
		}
		rs.next();
		Company company = new Company.CompanyBuilder(rs.getString("name")).withID(rs.getLong("id")).build();
		return Optional.of(company);
	}
	
	public ArrayList<Company> mapToCompanyArray(ResultSet rs) throws SQLException {
		ArrayList<Company> companies = new ArrayList<Company>();
		while (rs.next()) {
			companies.add(new Company.CompanyBuilder(rs.getString("name")).withID(rs.getLong("id")).build());
		}
		return companies;
	}
	
}
