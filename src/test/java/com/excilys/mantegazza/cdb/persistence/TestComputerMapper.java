package com.excilys.mantegazza.cdb.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.persistence.mappers.ComputerMapper;

public class TestComputerMapper {
	private final String queryGetByName = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.name=?";
	private final String getLastComputers = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.id>=2000";
	private final String insertTestComputer = "INSERT INTO computer (id, name, introduced, discontinued, company_id) VALUES (?,?,?,?,?)";
	private final String deleteTestComputer = "DELETE FROM computer WHERE name=?";
	private final String deleteLastComputers = "DELETE FROM computer WHERE id>=2000";
	
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private DBConnectionManager connectionManager = new DBConnectionManager();
	private Connection co;
	private ComputerMapper computerMapperSUT = new ComputerMapper();
	
	@BeforeEach
	public void initializeConnection() throws SQLException {
		co = connectionManager.getNewConnection();
	}
	
	@AfterEach
	public void closeConnection() throws SQLException {
		co.close();
	}
	
	@Test
	public void testComputerMappedFromDatabase() throws SQLException {
		Company testCompany = new Company.CompanyBuilder("Test Company").withID(1000).build();
		PreparedStatement createCompany = co.prepareStatement("INSERT INTO company (id, name) VALUES (?, ?);");
		createCompany.setLong(1, testCompany.getID());
		createCompany.setString(2, testCompany.getName());
		createCompany.executeUpdate();
		createCompany.close();
		
		Computer sourceComputer = new Computer.ComputerBuilder("Test computer")
				.withID(2000)
				.withIntroduced(LocalDate.of(2000, 9, 14))
				.withDiscontinued(LocalDate.of(2022, 2, 11))
				.withCompany(testCompany)
				.build();
		
		PreparedStatement psInsert = co.prepareStatement(insertTestComputer);
		psInsert.setLong(1, sourceComputer.getID());
		psInsert.setString(2, sourceComputer.getName());
		psInsert.setString(3, df.format(sourceComputer.getIntroductionDate()));
		psInsert.setString(4, df.format(sourceComputer.getDiscontinuationDate()));
		psInsert.setLong(5, testCompany.getID());
		psInsert.executeUpdate();
		psInsert.close();
		
		PreparedStatement psGet = co.prepareStatement(queryGetByName);				
		psGet.setString(1, sourceComputer.getName());
		ResultSet rs = psGet.executeQuery();
		Optional<Computer> mappedComputer = computerMapperSUT.mapToComputer(rs);
		rs.close();
		psGet.close();
		
		assertTrue(mappedComputer.isPresent());
		assertEquals(sourceComputer, mappedComputer.get());
		
		PreparedStatement psDelete = co.prepareStatement(deleteTestComputer);
		psDelete.setString(1, sourceComputer.getName());
		psDelete.executeUpdate();
		psDelete.close();
		PreparedStatement deleteCompany = co.prepareStatement("DELETE FROM company WHERE id=?;");
		deleteCompany.setLong(1, testCompany.getID());
		deleteCompany.executeUpdate();
		deleteCompany.close();
	}
	
	@Test
	public void mapComputerWithNullFields() throws SQLException {
		Computer sourceComputer = new Computer.ComputerBuilder("Test computer")
				.withID(2000)
				.build();
		
		PreparedStatement psInsert = co.prepareStatement(insertTestComputer);
		psInsert.setLong(1, sourceComputer.getID());
		psInsert.setString(2, sourceComputer.getName());
		psInsert.setNull(3, Types.TIMESTAMP);
		psInsert.setNull(4, Types.TIMESTAMP);
		psInsert.setNull(5, Types.BIGINT);
		psInsert.executeUpdate();
		psInsert.close();
		
		PreparedStatement psGet = co.prepareStatement(queryGetByName);				
		psGet.setString(1, sourceComputer.getName());
		ResultSet rs = psGet.executeQuery();
		Optional<Computer> mappedComputer = computerMapperSUT.mapToComputer(rs);
		rs.close();
		psGet.close();
		
		assertTrue(mappedComputer.isPresent());
		assertEquals(sourceComputer, mappedComputer.get());
		
		PreparedStatement psDelete = co.prepareStatement(deleteTestComputer);
		psDelete.setString(1, sourceComputer.getName());
		psDelete.executeUpdate();
		psDelete.close();
	}
	
	@Test
	public void mapNonExistingComputer() throws SQLException {
		Computer sourceComputer = new Computer.ComputerBuilder("Test computer").build();
		
		PreparedStatement psGet = co.prepareStatement(queryGetByName);				
		psGet.setString(1, sourceComputer.getName());
		ResultSet rs = psGet.executeQuery();
		Optional<Computer> mappedComputer = computerMapperSUT.mapToComputer(rs);
		rs.close();
		psGet.close();
		
		assertTrue(mappedComputer.isEmpty());
	}
	
	@Test
	public void mapComputerArray() throws SQLException {
		Company testCompany = new Company.CompanyBuilder("Test Company").withID(1000).build();
		PreparedStatement createCompany = co.prepareStatement("INSERT INTO company (id, name) VALUES (?, ?);");
		createCompany.setLong(1, testCompany.getID());
		createCompany.setString(2, testCompany.getName());
		createCompany.executeUpdate();
		createCompany.close();
		
		Computer computer1 = new Computer.ComputerBuilder("Test Computer 1").withID(2000).build();
		Computer computer2 = new Computer.ComputerBuilder("Test Computer 2").withID(2001)
				.withIntroduced(LocalDate.of(1970, 01, 01)).build();
		Computer computer3 = new Computer.ComputerBuilder("Test Computer 3").withID(2002)
				.withDiscontinued(LocalDate.of(2011, 11, 11)).build();
		Computer computer4 = new Computer.ComputerBuilder("Test Computer 4").withID(2003)
				.withIntroduced(LocalDate.of(1970, 01, 01))
				.withDiscontinued(LocalDate.of(2011, 11, 11))
				.withCompany(testCompany)
				.build();
		ArrayList<Computer> sourceComputers = new ArrayList<Computer>();
		sourceComputers.add(computer1);
		sourceComputers.add(computer2);
		sourceComputers.add(computer3);
		sourceComputers.add(computer4);
		
		PreparedStatement psInsert = co.prepareStatement(insertTestComputer);
		psInsert.setLong(1, computer1.getID());
		psInsert.setString(2, computer1.getName());
		psInsert.setNull(3, Types.TIMESTAMP);
		psInsert.setNull(4, Types.TIMESTAMP);
		psInsert.setNull(5, Types.BIGINT);
		psInsert.addBatch();
		
		psInsert.setLong(1, computer2.getID());
		psInsert.setString(2, computer2.getName());
		psInsert.setString(3, df.format(computer2.getIntroductionDate()));
		psInsert.setNull(4, Types.TIMESTAMP);
		psInsert.setNull(5, Types.BIGINT);
		psInsert.addBatch();
		
		psInsert.setLong(1, computer3.getID());
		psInsert.setString(2, computer3.getName());
		psInsert.setNull(3, Types.TIMESTAMP);
		psInsert.setString(4, df.format(computer3.getDiscontinuationDate()));
		psInsert.setNull(5, Types.BIGINT);
		psInsert.addBatch();
		
		psInsert.setLong(1, computer4.getID());
		psInsert.setString(2, computer4.getName());
		psInsert.setString(3, df.format(computer4.getIntroductionDate()));
		psInsert.setString(4, df.format(computer4.getDiscontinuationDate()));
		psInsert.setLong(5, computer4.getCompany().getID());
		psInsert.addBatch();
		
		psInsert.executeBatch();
		psInsert.close();
		
		
		PreparedStatement psGet = co.prepareStatement(getLastComputers);
		ResultSet rs = psGet.executeQuery();
		ArrayList<Computer> mappedComputers = computerMapperSUT.mapToComputerArray(rs);
		assertEquals(sourceComputers, mappedComputers);
		
		
		PreparedStatement psDelete = co.prepareStatement(deleteLastComputers);
		psDelete.executeUpdate();
		psDelete.close();
		PreparedStatement deleteCompany = co.prepareStatement("DELETE FROM company WHERE id=?;");
		deleteCompany.setLong(1, testCompany.getID());
		deleteCompany.executeUpdate();
		deleteCompany.close();
	}
	
	@Test
	public void mapEmptyArray() throws SQLException {
		PreparedStatement psGet = co.prepareStatement(getLastComputers);
		ResultSet rs = psGet.executeQuery();
		ArrayList<Computer> mappedComputers = computerMapperSUT.mapToComputerArray(rs);
		assertTrue(mappedComputers.isEmpty());
	}
}
