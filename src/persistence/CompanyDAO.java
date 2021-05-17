package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import model.Company;

public class CompanyDAO {

	private DBConnectionManager dbManager;
	private CompanyMapper mapper = new CompanyMapper();
	
	private final String queryGetByID = "SELECT id, name FROM company WHERE id=?";
	private final String queryGetByName = "SELECT id, name FROM company WHERE name=?";
	private final String queryGetAll = "SELECT id, name FROM company";
	
	public void setDatabaseManager(DBConnectionManager databaseManager) {
		this.dbManager = databaseManager;
	}
	
	public Optional<Company> getByID(long id) throws SQLException {
		Connection co = this.dbManager.getNewConnection();
		PreparedStatement ps = co.prepareStatement(queryGetByID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		Optional<Company> company = mapper.mapToCompany(rs);
		
		rs.close();
		ps.close();
		co.close();
		return company;
	}
	
	public Optional<Company> getByName(String name) throws SQLException {
		Connection co = this.dbManager.getNewConnection();
		PreparedStatement ps = co.prepareStatement(queryGetByName);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		Optional<Company> company = mapper.mapToCompany(rs);
		
		rs.close();
		ps.close();
		co.close();
		return company;
	}
	
	public ArrayList<Company> getAll() throws SQLException {
		Connection co = this.dbManager.getNewConnection();
		PreparedStatement ps = co.prepareStatement(queryGetAll);
		ResultSet rs = ps.executeQuery();
		ArrayList<Company> companies = mapper.mapToCompanyArray(rs);
		
		rs.close();
		ps.close();
		co.close();
		return companies;
	}
	
}
