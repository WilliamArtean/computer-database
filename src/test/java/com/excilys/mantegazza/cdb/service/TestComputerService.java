package com.excilys.mantegazza.cdb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excilys.mantegazza.cdb.exceptions.InconsistentDatesException;
import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;

@ExtendWith(MockitoExtension.class)
public class TestComputerService {

	@Mock
	private ComputerDAO dao;
	@Mock
	private CompanyService companyService;
	@InjectMocks
	private ComputerService service;

	private ArrayList<Computer> computerDB;
	
	@BeforeEach
	public void setUpService() {
		computerDB = new ArrayList<Computer>();
		computerDB.add(new Computer.ComputerBuilder("Computer 1").withID(1001).build());
		computerDB.add(new Computer.ComputerBuilder("Computer 2").withID(1002).build());
		computerDB.add(new Computer.ComputerBuilder("Computer 3").withID(1003).build());
		computerDB.add(new Computer.ComputerBuilder("Computer 4").withID(1004).build());
		
		service = new ComputerService();
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testGetByName() {
		when(dao.getByName(computerDB.get(0).getName()))
		.thenReturn(Optional.of(computerDB.get(0)));
		
		Optional<Computer> computer1 = Optional.of(computerDB.get(0));
		Optional<Computer> returnedComputerByName = service.getComputer(computer1.get().getName());
		assertEquals(computer1, returnedComputerByName);
	}
	
	@Test
	public void testGetByID() {
		when(dao.getByID(computerDB.get(0).getID()))
		.thenReturn(Optional.of(computerDB.get(0)));

		Optional<Computer> computer1 = Optional.of(computerDB.get(0));
		Optional<Computer> returnedComputerByID = service.getComputer(computer1.get().getID());
		assertEquals(computer1, returnedComputerByID);
	}
	
	@Test
	public void testGetAll() {
		when(dao.getAll()).thenReturn(computerDB);
		ArrayList<Computer> computersReturned = service.getAllComputers();
		assertEquals(computersReturned, computerDB);
	}
	
	@Test
	public void testCount() {
		when(dao.getCount()).thenReturn(computerDB.size());
		assertEquals(service.getCount(), computerDB.size());
	}
	
	@Test
	public void testUpdateComputer() throws InconsistentDatesException {
		long id = 999999L;
		String newName = "New computer";
		LocalDate introduced = LocalDate.of(2000, 10, 31);
		LocalDate discontinued = LocalDate.of(2020, 8, 9);
		Company company = new Company.CompanyBuilder("Company 1").build();
		
		Computer updatedComputer = new Computer.ComputerBuilder()
				.withName(newName)
				.withID(id)
				.withIntroduced(introduced)
				.withDiscontinued(discontinued)
				.withCompany(company)
				.build();
		
		when(companyService.getCompany(company.getName())).thenReturn(Optional.of(company));
		
		service.update(id, updatedComputer);
		verify(dao).update(id, updatedComputer);
	}
	
	@Test
	public void testUpdateEmptyComputer() throws InconsistentDatesException {
		long id = 999999L;
		Computer updatedComputer = new Computer.ComputerBuilder().withID(id).build();
		
		service.update(id, updatedComputer);
		verify(dao).update(id, updatedComputer);
	}
	
	@Test
	public void updateWithNonExistingCompany() throws InconsistentDatesException {
		long id = 999999L;
		Computer computerWithoutCompany = new Computer.ComputerBuilder().withID(id).build();
		Company ghostCompany = new Company.CompanyBuilder("Non-existing company").build();
		
		when(companyService.getCompany(ghostCompany.getName())).thenReturn(Optional.empty());
		
		service.update(id, computerWithoutCompany);
		verify(dao).update(id, computerWithoutCompany);
	}
	
	@Test
	public void deleteComputerByName() {
		String name = computerDB.get(0).getName();
		service.delete(name);
		verify(dao).delete(name);
	}
	
	@Test
	public void deleteComputerByID() {
		long id = 1;
		service.delete(id);
		verify(dao).delete(id);
	}
	
}
