package com.excilys.mantegazza.cdb.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excilys.mantegazza.cdb.model.Company;

public class TestCompanyDAO {

	private final String queryGetByID = "SELECT id, name FROM company WHERE id=?";
	private final String queryGetByName = "SELECT id, name FROM company WHERE name=?";
	private final String queryGetAll = "SELECT id, name FROM company";
	private final String queryGetSelection = "SELECT id, name FROM company ORDER BY id LIMIT ? OFFSET ?";
	private final String queryGetCount = "SELECT COUNT(id) AS rowcount FROM company";
	private final String insertTestCompany = "INSERT INTO company (id, name) VALUES (?,?)";
	
	private CompanyDAO companyDAOSUT = new CompanyDAO();
	private DBConnectionManager connectionManager = new DBConnectionManager();
	private Connection co;
	
	@BeforeEach
	public void initializeConnection() throws SQLException {
		companyDAOSUT.setDatabaseManager(connectionManager);
		co = connectionManager.getNewConnection();
	}
	
	@AfterEach
	public void closeConnection() throws SQLException {
		co.close();
	}
	
	@Test
	public void getCompanyByID() throws SQLException {
		Company sourceCompany = new Company.CompanyBuilder("Nintendo").withID(24).build();
		
		Optional<Company> fetchedCompany = companyDAOSUT.getByID(sourceCompany.getID());
		assertTrue(fetchedCompany.isPresent());
		assertEquals(sourceCompany, fetchedCompany.get());
	}
	
}
