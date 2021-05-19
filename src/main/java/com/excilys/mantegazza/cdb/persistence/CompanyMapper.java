package com.excilys.mantegazza.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import com.excilys.mantegazza.cdb.model.Company;

public class CompanyMapper {

	public Optional<Company> mapToCompany(ResultSet rs) throws SQLException {
		if (!rs.isBeforeFirst()) {
			return Optional.empty();
		}
		rs.next();
		Company company = new Company(rs.getLong("id"), rs.getString("name"));
		return Optional.of(company);
	}
	
	public ArrayList<Company> mapToCompanyArray(ResultSet rs) throws SQLException {
		ArrayList<Company> companies = new ArrayList<Company>();
		while (rs.next()) {
			companies.add(new Company(rs.getLong("id"), rs.getString("name")));
		}
		return companies;
	}
	
}
