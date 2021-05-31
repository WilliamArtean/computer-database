package com.excilys.mantegazza.cdb.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excilys.mantegazza.cdb.model.Company;

public class TestCompanyDAO {

	private final String queryGetAll = "SELECT id, name FROM company";
	private final String queryGetSelection = "SELECT id, name FROM company ORDER BY id LIMIT ? OFFSET ?";
	
	private CompanyDAO companyDAOSUT = new CompanyDAO();
	private DBConnectionManager connectionManager = DBConnectionManager.getInstance();
	private Connection co;
	
	@BeforeEach
	public void initializeConnection() throws SQLException {
		co = connectionManager.getNewConnection();
	}
	
	@AfterEach
	public void closeConnection() throws SQLException {
		co.close();
	}
	
	@Test
	public void getCompanyByID() {
		Company sourceCompany = new Company.CompanyBuilder("Nintendo").withID(24).build();
		
		Optional<Company> fetchedCompany = companyDAOSUT.getByID(sourceCompany.getID());
		assertTrue(fetchedCompany.isPresent());
		assertEquals(sourceCompany, fetchedCompany.get());
	}
	
	@Test
	public void getNonExistingCompanyByID() {
		Company sourceCompany = new Company.CompanyBuilder("Ghost Company").withID(9999).build();
		
		Optional<Company> fetchedCompany = companyDAOSUT.getByID(sourceCompany.getID());
		assertTrue(fetchedCompany.isEmpty());
	}
	
	@Test
	public void getCompanyByName() {
		Company sourceCompany = new Company.CompanyBuilder("Nintendo").withID(24).build();
		
		Optional<Company> fetchedCompany = companyDAOSUT.getByName(sourceCompany.getName());
		assertTrue(fetchedCompany.isPresent());
		assertEquals(sourceCompany, fetchedCompany.get());
	}
	
	@Test
	public void getNonExistingCompanyByName() {
		Company sourceCompany = new Company.CompanyBuilder("Ghost Company").withID(9999).build();
		
		Optional<Company> fetchedCompany = companyDAOSUT.getByName(sourceCompany.getName());
		assertTrue(fetchedCompany.isEmpty());
	}
	
	@Test
	public void getAllCompanies() throws SQLException {		
		PreparedStatement ps = co.prepareStatement(queryGetAll);
		ResultSet rs = ps.executeQuery();
		
		ArrayList<Company> sourceCompanies = new ArrayList<Company>();
		while (rs.next()) {
			sourceCompanies.add(
					new Company.CompanyBuilder(rs.getString("name"))
					.withID(rs.getLong("id"))
					.build());
		}
		
		ArrayList<Company> fetchedCompanies = companyDAOSUT.getAll();
		assertFalse(fetchedCompanies.isEmpty());
		assertEquals(sourceCompanies, fetchedCompanies);
	}
	
	@Test
	public void getCompanySelection() throws SQLException {
		PreparedStatement ps = co.prepareStatement(queryGetSelection);
		ps.setInt(1, 10);
		ps.setInt(2, 20);
		ResultSet rs = ps.executeQuery();
		
		ArrayList<Company> sourceCompanies = new ArrayList<Company>();
		while (rs.next()) {
			sourceCompanies.add(
					new Company.CompanyBuilder(rs.getString("name"))
					.withID(rs.getLong("id"))
					.build());
		}
		
		ArrayList<Company> fetchedCompanies = companyDAOSUT.getSelection(10, 20);
		assertFalse(fetchedCompanies.isEmpty());
		assertEquals(sourceCompanies, fetchedCompanies);
	}
	
	@Test
	public void getCompanyCount() {
		assertEquals(42, companyDAOSUT.getCount());
	}
	
}
