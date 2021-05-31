package com.excilys.mantegazza.cdb.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.persistence.mappers.CompanyMapper;

@ExtendWith(MockitoExtension.class)
public class TestCompanyMapper {
	
	private final String queryGetByName = "SELECT id, name FROM company WHERE name=?";
	private final String getLastCompanies = "SELECT id, name FROM company WHERE id>=1000";
	private final String insertTestCompany = "INSERT INTO company (id, name) VALUES (?,?)";
	private final String deleteTestCompany = "DELETE FROM company WHERE name=?";
	
	private DBConnectionManager connectionManager = DBConnectionManager.getInstance();;
	private CompanyMapper companyMapperSUT = new CompanyMapper();
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
	public void testCompanyMappedFromDatabase() throws SQLException {
		Company sourceCompany = new Company.CompanyBuilder("Test Company").withID(1000).build();
		
		PreparedStatement psInsert = co.prepareStatement(insertTestCompany);
		psInsert.setLong(1, sourceCompany.getID());
		psInsert.setString(2, sourceCompany.getName());
		psInsert.executeUpdate();
		psInsert.close();
		
		PreparedStatement psGet = co.prepareStatement(queryGetByName);				
		psGet.setString(1, sourceCompany.getName());
		ResultSet rs = psGet.executeQuery();
		Optional<Company> mappedCompany = companyMapperSUT.mapToCompany(rs);
		rs.close();
		psGet.close();
		
		assertTrue(mappedCompany.isPresent());
		assertEquals(sourceCompany, mappedCompany.get());
		
		PreparedStatement psDelete = co.prepareStatement(deleteTestCompany);
		psDelete.setString(1, sourceCompany.getName());
		psDelete.executeUpdate();
		psDelete.close();
	}
	
	@Test
	public void mapVoidCompany() throws SQLException {
		Company sourceCompany = new Company.CompanyBuilder("Test Company").withID(1000).build();
		
		PreparedStatement psGet = co.prepareStatement(queryGetByName);				
		psGet.setString(1, sourceCompany.getName());
		ResultSet rs = psGet.executeQuery();
		Optional<Company> mappedCompany = companyMapperSUT.mapToCompany(rs);
		rs.close();
		psGet.close();
		
		assertTrue(mappedCompany.isEmpty());
	}
	
	@Test
	public void mapCompanyArray() throws SQLException {
		Company company1 = new Company.CompanyBuilder("Test Company 1").withID(1000).build();
		Company company2 = new Company.CompanyBuilder("Test Company 2").withID(1001).build();
		Company company3 = new Company.CompanyBuilder("Test Company 3").withID(1002).build();
		Company company4 = new Company.CompanyBuilder("Test Company 4").withID(1003).build();
		ArrayList<Company> sourceCompanies = new ArrayList<Company>();
		sourceCompanies.add(company1);
		sourceCompanies.add(company2);
		sourceCompanies.add(company3);
		sourceCompanies.add(company4);
		
		PreparedStatement psInsert = co.prepareStatement(insertTestCompany);
		psInsert.setLong(1, company1.getID());
		psInsert.setString(2, company1.getName());
		psInsert.addBatch();
		
		psInsert.setLong(1, company2.getID());
		psInsert.setString(2, company2.getName());
		psInsert.addBatch();
		
		psInsert.setLong(1, company3.getID());
		psInsert.setString(2, company3.getName());
		psInsert.addBatch();
		
		psInsert.setLong(1, company4.getID());
		psInsert.setString(2, company4.getName());
		psInsert.addBatch();
		
		psInsert.executeBatch();
		psInsert.close();
		
		
		PreparedStatement psGet = co.prepareStatement(getLastCompanies);
		ResultSet rs = psGet.executeQuery();
		ArrayList<Company> mappedCompanies = companyMapperSUT.mapToCompanyArray(rs);
		assertEquals(sourceCompanies, mappedCompanies);
		
		
		PreparedStatement psDelete = co.prepareStatement(deleteTestCompany);
		psDelete.setString(1, company1.getName());
		psDelete.addBatch();
		
		psDelete.setString(1, company2.getName());
		psDelete.addBatch();
		
		psDelete.setString(1, company3.getName());
		psDelete.addBatch();
		
		psDelete.setString(1, company4.getName());
		psDelete.addBatch();
		
		psDelete.executeBatch();
		psDelete.close();
	}
	
	@Test
	public void mapEmptyArray() throws SQLException {
		PreparedStatement psGet = co.prepareStatement(getLastCompanies);
		ResultSet rs = psGet.executeQuery();
		ArrayList<Company> mappedCompanies = companyMapperSUT.mapToCompanyArray(rs);
		assertTrue(mappedCompanies.isEmpty());
	}
	
}
