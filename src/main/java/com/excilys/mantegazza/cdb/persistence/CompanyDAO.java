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
import com.excilys.mantegazza.cdb.persistence.mappers.CompanyMapper;

public class CompanyDAO {

	private Logger logger = LoggerFactory.getLogger(CompanyDAO.class);
	private DBConnectionManager dbManager;
	private CompanyMapper mapper = new CompanyMapper();
	
	private final String queryGetByID = "SELECT id, name FROM company WHERE id=?";
	private final String queryGetByName = "SELECT id, name FROM company WHERE name=?";
	private final String queryGetAll = "SELECT id, name FROM company";
	private final String queryGetSelection = "SELECT id, name FROM company ORDER BY id LIMIT ? OFFSET ?";
	private final String queryGetCount = "SELECT COUNT(id) AS rowcount FROM company";
	
	/**
	 * Set the DatabageManager that will be used to get a connection to the database.
	 * @param databaseManager The Database Manager that will creates the connections to the database
	 */
	public void setDatabaseManager(DBConnectionManager databaseManager) {
		this.dbManager = databaseManager;
	}
	
	/**
	 * Execute a SQL query to fetch the company with the required id.
	 * @param id The id of the company to return
	 * @return An Optional containing a Company object matching the id, or an empty Optional if the company could not be found
	 */
	public Optional<Company> getByID(long id) {
		Optional<Company> company = Optional.empty();
		try (
				Connection co = dbManager.getNewConnection();
				PreparedStatement ps = co.prepareStatement(queryGetByID);
			) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			company = mapper.mapToCompany(rs);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return company;
	}
	
	/**
	 * Execute a SQL query to fetch the company with the required name.
	 * @param name The name of the company to return
	 * @return An Optional containing a Company object matching the name, or an empty Optional if the company could not be found
	 */
	public Optional<Company> getByName(String name) {
		Optional<Company> company = Optional.empty();
		try (
				Connection co = this.dbManager.getNewConnection();
				PreparedStatement ps = co.prepareStatement(queryGetByName);
			) {
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			company = mapper.mapToCompany(rs);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		
		return company;
	}
	
	/**
	 * Execute a SQL query to fetch all the companies present in the company database.
	 * @return An ArrayList of all the Company objects in the database
	 */
	public ArrayList<Company> getAll() {
		ArrayList<Company> companies = new ArrayList<Company>();
		try (
				Connection co = this.dbManager.getNewConnection();
				PreparedStatement ps = co.prepareStatement(queryGetAll);
				ResultSet rs = ps.executeQuery();
			) {
			companies = mapper.mapToCompanyArray(rs);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return companies;
	}
	
	/**
	 * Execute a SQL query to fetch a selected number of companies from the database.
	 * The companies are ordered by their id.
	 * @param numberToReturn The number of companies to fetch from the database
	 * @param offset The index of row from which to start the selection. 0 to start from the first row (included).
	 * @return An ArrayList of Company objects in the database, of size numberToReturn at most (less if the end of the database has been reached)
	 */
	public ArrayList<Company> getSelection(int numberToReturn, int offset) {
		ArrayList<Company> companies = new ArrayList<Company>();
		try (
				Connection co = this.dbManager.getNewConnection();
				PreparedStatement ps = co.prepareStatement(queryGetSelection);
			) {
			ps.setInt(1, numberToReturn);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			companies = mapper.mapToCompanyArray(rs);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return companies;
	}
	
	/**
	 * Execute a SQL query to fetch the nubmer of Company objects in the database.
	 * @return An int corresponding to the nubmer of rows in the company database
	 */
	public int getCount() {
		int count = 0;
		try {
			Connection co = this.dbManager.getNewConnection();
			PreparedStatement ps = co.prepareStatement(queryGetCount);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			count = rs.getInt("rowcount");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return count;
	}
	
}
