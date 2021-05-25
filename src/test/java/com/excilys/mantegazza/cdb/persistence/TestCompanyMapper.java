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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excilys.mantegazza.cdb.model.Company;

@ExtendWith(MockitoExtension.class)
public class TestCompanyMapper {
	
	private final String queryGetByName = "SELECT id, name FROM company WHERE name=?";
	private final String insertTestCompany = "INSERT INTO company (id, name) VALUES (?,?)";
	private final String deleteTestCompany = "DELETE FROM company WHERE name=?";
	
	private DBConnectionManager connectionManager = new DBConnectionManager();
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
	
}
