package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Company;

public class CompanyDAO {

	private Connection co = null;
	
	private final String queryGetByID = "SELECT id, name FROM company WHERE id=?";
	private final String queryGetByName = "SELECT id, name FROM company WHERE name=?";
	private final String queryGetAll = "SELECT id, name FROM company";
	
	public void setConnection(Connection connection) {
		this.co = connection;
	}
	
	
	public Company getByID(long id) throws SQLException {
		PreparedStatement ps = this.co.prepareStatement(queryGetByID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		
		if (!rs.isBeforeFirst()) return null;
		
		rs.next();
		Company company = new Company(rs.getLong("id"), rs.getString("name"));
		ps.close();
		return company;
	}
	
	public Company getByName(String name) throws SQLException {
		PreparedStatement ps = this.co.prepareStatement(queryGetByName);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		
		if (!rs.isBeforeFirst()) return null;
		
		rs.next();
		Company company = new Company(rs.getLong("id"), rs.getString("name"));
		ps.close();
		return company;
	}
	
	public ArrayList<Company> getAll() throws SQLException {
		PreparedStatement ps = this.co.prepareStatement(queryGetAll);
		ResultSet rs = ps.executeQuery();
		
		ArrayList<Company> companies = new ArrayList<Company>();
		
		while (rs.next()) {
			companies.add(new Company(rs.getLong("id"), rs.getString("name")));
		}
		
		rs.close();
		ps.close();
		return companies;
	}
	
}
