package persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Company;

public class CompanyDAO {

	private Connection co;
	
	public void setConnection(Connection connection) {
		this.co = connection;
	}
	
	
	public Company getByID(long id) throws SQLException {
		String getByIdQuery = "SELECT * FROM company WHERE id=" + id + ";";
		Statement st = this.co.createStatement();
		ResultSet rs = st.executeQuery(getByIdQuery);
		
		if (!rs.isBeforeFirst()) return null;
		
		rs.next();
		Company company = new Company(rs.getLong("id"), rs.getString("name"));
		st.close();
		return company;
	}
	
	public Company getByName(String name) throws SQLException {
		String getByIdQuery = "SELECT * FROM company WHERE name=\"" + name + "\";";
		Statement st = this.co.createStatement();
		ResultSet rs = st.executeQuery(getByIdQuery);
		
		if (!rs.isBeforeFirst()) return null;
		
		rs.next();
		Company company = new Company(rs.getLong("id"), rs.getString("name"));
		st.close();
		return company;
	}
	
	public ArrayList<Company> getAll() throws SQLException {
		String getAllQuery = "SELECT * FROM company;";
		Statement st = this.co.createStatement();
		ResultSet results = st.executeQuery(getAllQuery);
		
		ArrayList<Company> companies = new ArrayList<Company>();
		
		while (results.next()) {
			companies.add(new Company(results.getLong("id"), results.getString("name")));
		}
		
		st.close();
		return companies;
	}
	
}
