package com.excilys.mantegazza.cdb.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;

public class TestComputerDAO {

	private final String queryGetAll = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id";
	private final String queryGetSelection = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id ORDER BY computer.id LIMIT ? OFFSET ?";
	private final String queryGetCount = "SELECT COUNT(id) AS rowcount FROM computer";
	private final String queryGetByName = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.name=?";
	
	private ComputerDAO computerDAOSUT = new ComputerDAO();
	private DBConnectionManager connectionManager = new DBConnectionManager();
	private Connection co;
	
	@BeforeEach
	public void initializeConnection() throws SQLException {
		computerDAOSUT.setDatabaseManager(connectionManager);
		co = connectionManager.getNewConnection();
	}
	
	@AfterEach
	public void closeConnection() throws SQLException {
		co.close();
	}
	
	@Test
	public void getComputerByID() {
		Company nintendo = new Company.CompanyBuilder("Nintendo").withID(24).build();
		Computer snes = new Computer.ComputerBuilder("Super Nintendo Entertainment System")
				.withID(154)
				.withIntroduced(LocalDate.of(1991, 8, 1))
				.withDiscontinued(LocalDate.of(1999, 1, 1))
				.withCompany(nintendo)
				.build();
		
		Optional<Computer> fetchedSnes = computerDAOSUT.getByID(snes.getID());
		assertTrue(fetchedSnes.isPresent());
		assertEquals(snes, fetchedSnes.get());
	}
	
	@Test
	public void getNonExistingComputerByID() {
		Computer ghostComputer = new Computer.ComputerBuilder("Ghost Computer").withID(9999).build();
		
		Optional<Computer> fetchedComputer = computerDAOSUT.getByID(ghostComputer.getID());
		assertTrue(fetchedComputer.isEmpty());
	}
	
	@Test
	public void getComputerByName() {
		Company nintendo = new Company.CompanyBuilder("Nintendo").withID(24).build();
		Computer snes = new Computer.ComputerBuilder("Super Nintendo Entertainment System")
				.withID(154)
				.withIntroduced(LocalDate.of(1991, 8, 1))
				.withDiscontinued(LocalDate.of(1999, 1, 1))
				.withCompany(nintendo)
				.build();
		
		Optional<Computer> fetchedSnes = computerDAOSUT.getByName(snes.getName());
		assertTrue(fetchedSnes.isPresent());
		assertEquals(snes, fetchedSnes.get());
	}
	
	@Test
	public void getNonExistingComputerByName() {
		Computer ghostComputer = new Computer.ComputerBuilder("Ghost Computer").withID(9999).build();
		
		Optional<Computer> fetchedComputer = computerDAOSUT.getByName(ghostComputer.getName());
		assertTrue(fetchedComputer.isEmpty());
	}
	
	@Test
	public void getAllComputers() throws SQLException {
		ComputerMapper mapper = new ComputerMapper();
		PreparedStatement ps = co.prepareStatement(queryGetAll);
		ResultSet rs = ps.executeQuery();
		
		ArrayList<Computer> sourceComputers = mapper.mapToComputerArray(rs);
		
		ArrayList<Computer> fetchedComputers = computerDAOSUT.getAll();
		assertFalse(fetchedComputers.isEmpty());
		assertEquals(sourceComputers, fetchedComputers);
	}
	
	@Test
	public void getComputerSelection() throws SQLException {
		ComputerMapper mapper = new ComputerMapper();
		PreparedStatement ps = co.prepareStatement(queryGetSelection);
		ps.setInt(1, 10);
		ps.setInt(2, 20);
		ResultSet rs = ps.executeQuery();
		
		ArrayList<Computer> sourceComputers = mapper.mapToComputerArray(rs);
		
		ArrayList<Computer> fetchedComputers = computerDAOSUT.getSelection(10, 20);
		assertFalse(fetchedComputers.isEmpty());
		assertEquals(sourceComputers, fetchedComputers);
	}
	
	@Test
	public void getComputerCount() throws SQLException {
		PreparedStatement ps = co.prepareStatement(queryGetCount);
		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt("rowcount");
		rs.close();
		
		assertEquals(count, computerDAOSUT.getCount());
	}
	
	@Test
	public void testComputerInsertion() throws SQLException {
		Company nintendo = new Company.CompanyBuilder("Nintendo").withID(24).build();
		Computer newComputer = new Computer.ComputerBuilder("NewComputer")
				.withIntroduced(LocalDate.of(2021, 8, 1))
				.withDiscontinued(LocalDate.of(2030, 4, 16))
				.withCompany(nintendo)
				.build();
		computerDAOSUT.create(newComputer);
		
		PreparedStatement ps = co.prepareStatement(queryGetByName);
		ps.setString(1, newComputer.getName());
		ResultSet rs = ps.executeQuery();
		
		ComputerMapper mapper = new ComputerMapper();
		Optional<Computer> addedComputer = mapper.mapToComputer(rs);
		
		assertTrue(addedComputer.isPresent());
		newComputer.setID(addedComputer.get().getID());
		assertEquals(newComputer, addedComputer.get());
		
	}
	
}
