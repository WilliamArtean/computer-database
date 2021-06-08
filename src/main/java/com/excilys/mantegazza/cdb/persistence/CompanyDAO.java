package com.excilys.mantegazza.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.persistence.mappers.CompanyMapper;

@Repository
public class CompanyDAO {

	@Autowired
	private CompanyMapper mapper;
	@Autowired
	private DataSource dataSource;
	private Logger logger = LoggerFactory.getLogger(CompanyDAO.class);
	
	private final String queryGetByID = "SELECT id, name FROM company WHERE id=?";
	private final String queryGetByName = "SELECT id, name FROM company WHERE name=?";
	private final String queryGetAll = "SELECT id, name FROM company";
	private final String queryGetSelection = "SELECT id, name FROM company ORDER BY id LIMIT ? OFFSET ?";
	private final String queryGetCount = "SELECT COUNT(id) AS rowcount FROM company";
	private final String queryDeleteAssociatedComputers = "DELETE computer FROM computer LEFT JOIN company ON (computer.company_id = company.id) WHERE company.name=?";
	private final String queryDeleteByName = "DELETE FROM company WHERE name=?";
	
	/**
	 * Execute a SQL query to fetch the company with the required id.
	 * @param id The id of the company to return
	 * @return An Optional containing a Company object matching the id, or an empty Optional if the company could not be found
	 */
	public Optional<Company> getByID(long id) {
		Optional<Company> company = Optional.empty();
		try (
				Connection co = dataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryGetByID);
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
				Connection co = dataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryGetByName);
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
				Connection co = dataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryGetAll);
			ResultSet rs = ps.executeQuery();
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
				Connection co = dataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryGetSelection);
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
		try (
				Connection co = dataSource.getConnection();
			) {
			PreparedStatement ps = co.prepareStatement(queryGetCount);
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			count = rs.getInt("rowcount");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return count;
	}
	
	public void delete(String name) {
		Connection co = null;
		try {
			co = dataSource.getConnection();
			co.setAutoCommit(false);
			
			PreparedStatement psComputers = co.prepareStatement(queryDeleteAssociatedComputers);
			psComputers.setString(1, name);
			psComputers.executeUpdate();
			logger.debug("Preparing query: {}", psComputers.toString());
			
			PreparedStatement psCompany = co.prepareStatement(queryDeleteByName);
			psCompany.setString(1, name);
			psCompany.executeUpdate();
			logger.debug("Preparing query: {}", psCompany.toString());
			
			co.commit();
			co.close();
			logger.debug("Deleted computers associated with company '{}' from computer", name);
			logger.debug("Deleted company '{}' from company", name);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			if (co != null) {
				try {
					logger.error("Transaction failed. Initiating rollback.");
					co.rollback();
				} catch (SQLException e1) {
					logger.error(e.getMessage());
				}
			}
		}
	}
	
}
