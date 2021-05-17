package persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Company;

public class CompanyMapper {

	public Company ResultSetToCompany(ResultSet rs) throws SQLException {
		if (!rs.isBeforeFirst()) return null;
		rs.next();
		Company company = new Company(rs.getLong("id"), rs.getString("name"));
		return company;
	}
	
	public ArrayList<Company> ResultSetToCompanyArray(ResultSet rs) throws SQLException {
		ArrayList<Company> companies = new ArrayList<Company>();
		while (rs.next()) {
			companies.add(new Company(rs.getLong("id"), rs.getString("name")));
		}
		return companies;
	}
	
}
