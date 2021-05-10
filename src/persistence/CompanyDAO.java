package persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Company;

public class CompanyDAO {

	private Connection co;
	private static CompanyDAO instance = new CompanyDAO();
	
	public static CompanyDAO getInstance() {
		return instance;
	}
	
	public void setConnection(Connection connection) {
		this.co = connection;
	}
	
	
	public Company getByID(long id) {
		return null;
	}
	
	public Company getByName(String name) {
		return null;
	}
	
	public ArrayList<Company> getAll() throws SQLException {
		String getAllQuery = "SELECT * FROM company";
		Statement st = this.co.createStatement();
		ResultSet results = st.executeQuery(getAllQuery);
		
		ArrayList<Company> companies = new ArrayList<Company>();
		
		while (results.next()) {
			companies.add(new Company(results.getString("name")));
		}
		
		return companies;
	}
	
}
