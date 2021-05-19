package com.excilys.mantegazza.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mantegazza.cdb.model.Company;

public class CompanyDAO {

	private Logger logger = LoggerFactory.getLogger(CompanyDAO.class);
	private DBConnectionManager dbManager;
	private CompanyMapper mapper = new CompanyMapper();
	
	private final String queryGetByID = "SELECT id, name FROM company WHERE id=?";
	private final String queryGetByName = "SELECT id, name FROM company WHERE name=?";
	private final String queryGetAll = "SELECT id, name FROM company";
	private final String queryGetSelection = "SELECT id, name FROM company ORDER BY id LIMIT ? OFFSET ?";
	private final String queryGetCount = "SELECT COUNT(id) AS rowcount FROM company";
	
	public void setDatabaseManager(DBConnectionManager databaseManager) {
		this.dbManager = databaseManager;
	}
	
	public Optional<Company> getByID(long id) {
		Optional<Company> company = Optional.empty();
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryGetByID);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			company = mapper.mapToCompany(rs);
			
			rs.close();
			ps.close();
			co.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return company;
	}
	
	public Optional<Company> getByName(String name) {
		Optional<Company> company = Optional.empty();
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryGetByName);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			company = mapper.mapToCompany(rs);
			
			rs.close();
			ps.close();
			co.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		
		return company;
	}
	
	public ArrayList<Company> getAll() {
		ArrayList<Company> companies = new ArrayList<Company>();
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryGetAll);
			ResultSet rs = ps.executeQuery();
			companies = mapper.mapToCompanyArray(rs);
			
			rs.close();
			ps.close();
			co.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return companies;
	}
	
	public ArrayList<Company> getSelection(int numberToReturn, int offset) {
		ArrayList<Company> companies = new ArrayList<Company>();
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryGetSelection);
			ps.setInt(1, numberToReturn);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			companies = mapper.mapToCompanyArray(rs);
			
			rs.close();
			ps.close();
			co.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return companies;
	}
	
	public int getCount() {
		int count = 0;
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryGetCount);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			count = rs.getInt("rowcount");
			
			rs.close();
			ps.close();
			co.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return count;
	}
	
}
