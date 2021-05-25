package com.excilys.mantegazza.cdb.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.excilys.mantegazza.cdb.exceptions.InconsistentDatesException;
import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;

import validator.DatesConsistencyValidator;

@RunWith(MockitoJUnitRunner.class)
public class TestComputerService {


	@Mock
	private ComputerDAO dao;
	@Mock
	private CompanyService companyService;
	@Mock
	private DatesConsistencyValidator datesValidator;
	@InjectMocks
	private ComputerService service;

	private ArrayList<Computer> computerDB;
	
	@Before
	public void setUpService() {
		computerDB = new ArrayList<Computer>();
		computerDB.add(new Computer.ComputerBuilder("Computer 1").build());
		computerDB.add(new Computer.ComputerBuilder("Computer 2").build());
		computerDB.add(new Computer.ComputerBuilder("Computer 3").build());
		computerDB.add(new Computer.ComputerBuilder("Computer 4").build());
		
		
		service = new ComputerService();
	}
	@Before
	public void initMocks() {
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
	public void testGetSelection() {
		ArrayList<Computer> computerSelection = new ArrayList<Computer>();
		computerSelection.add(computerDB.get(1));
		computerSelection.add(computerDB.get(2));
		
		when(dao.getSelection(2, 1)).thenReturn(computerSelection);
		
		ArrayList<Computer> computersReturned = service.getComputerSelection(2, 1);
		assertEquals(computersReturned, computerSelection);
	}
	
	@Test
	public void testCount() {
		when(dao.getCount()).thenReturn(computerDB.size());
		assertEquals(service.getCount(), computerDB.size());
	}
	
	@Test
	public void testCreateComputer() throws InconsistentDatesException {
		String name = "computer 1";
		Computer computerToCreate = new Computer.ComputerBuilder().withName(name).build();
		LocalDate introduced = LocalDate.of(2000, 10, 31);
		LocalDate discontinued = LocalDate.of(2020, 8, 9);
		Company company = new Company.CompanyBuilder("Company 1").build();
		
		computerToCreate.setIntroductionDate(introduced);
		computerToCreate.setDiscontinuationDate(discontinued);
		computerToCreate.setCompany(company);

		when(companyService.getCompany(company.getName())).thenReturn(Optional.of(company));
		service.create(name,
				Optional.of(introduced),
				Optional.of(discontinued),
				Optional.of(company.getName()));
		verify(dao).create(computerToCreate);
	}
	
	@Test
	public void testCreateEmptyComputer() throws InconsistentDatesException {
		String name = "Empty computer";
		Computer emptyComputer = new Computer.ComputerBuilder().withName(name).build();
		
		service.create(name, Optional.empty(), Optional.empty(), Optional.empty());
		verify(dao).create(emptyComputer);
	}
	
	@Test
	public void testUpdateComputer() throws InconsistentDatesException {
		String oldName = "Old computer";
		String newName = "New computer";
		LocalDate introduced = LocalDate.of(2000, 10, 31);
		LocalDate discontinued = LocalDate.of(2020, 8, 9);
		Company company = new Company.CompanyBuilder("Company 1").build();
		
		Computer updatedComputer = new Computer.ComputerBuilder()
				.withName(newName)
				.withIntroduced(introduced)
				.withDiscontinued(discontinued)
				.withCompany(company)
				.build();
		
		when(companyService.getCompany(company.getName())).thenReturn(Optional.of(company));
		
		service.update(oldName, Optional.of(newName), Optional.of(introduced), Optional.of(discontinued), Optional.of(company.getName()));
		verify(dao).update(oldName, updatedComputer);
	}
	
	@Test
	public void testUpdateEmptyComputer() throws InconsistentDatesException {
		String oldName = "Old computer";
		Computer updatedComputer = new Computer.ComputerBuilder().withName(oldName).build();
		
		service.update(oldName, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
		verify(dao).update(oldName, updatedComputer);
	}
	
	@Test
	public void testCreateWithSingleDate() throws InconsistentDatesException {
		String name = "computer 1";
		LocalDate introduced = LocalDate.of(2000, 10, 31);
		LocalDate discontinued = LocalDate.of(2020, 8, 9);
		
		Computer computerWithIntroduced = new Computer.ComputerBuilder().withName(name).withIntroduced(introduced).build();
		Computer computerWithDiscontinued = new Computer.ComputerBuilder().withName(name).withDiscontinued(discontinued).build();
		
		service.create(name, Optional.of(introduced), Optional.empty(), Optional.empty());
		verify(dao).create(computerWithIntroduced);
		
		service.create(name, Optional.empty(), Optional.of(discontinued), Optional.empty());
		verify(dao).create(computerWithDiscontinued);
	}
	
	@Test (expected = InconsistentDatesException.class)
	public void createWithInconsistentDates() throws InconsistentDatesException {
		String name = "New computer";
		LocalDate introduced = LocalDate.of(2020, 8, 9);
		LocalDate discontinued = LocalDate.of(2000, 10, 31);
		
		service.create(name, Optional.of(introduced), Optional.of(discontinued), Optional.empty());
	}
	
	@Test (expected = InconsistentDatesException.class)
	public void updateWithInconsistentDates() throws InconsistentDatesException {
		String oldName = "Old computer";
		LocalDate introduced = LocalDate.of(2020, 8, 9);
		LocalDate discontinued = LocalDate.of(2000, 10, 31);
		
		service.update(oldName, Optional.empty(), Optional.of(introduced), Optional.of(discontinued), Optional.empty());
	}
	
	@Test
	public void createWithNonExistingCompany() throws InconsistentDatesException {
		String name = "computer 1";
		Computer computerWithoutCompany = new Computer.ComputerBuilder().withName(name).build();
		Company ghostCompany = new Company.CompanyBuilder("Non-existing company").build();
		
		when(companyService.getCompany(ghostCompany.getName())).thenReturn(Optional.empty());
		
		service.create(name, Optional.empty(), Optional.empty(), Optional.of(ghostCompany.getName()));
		verify(dao).create(computerWithoutCompany);
	}
	
	@Test
	public void updateWithNonExistingCompany() throws InconsistentDatesException {
		String oldName = "computer 1";
		Computer computerWithoutCompany = new Computer.ComputerBuilder().withName(oldName).build();
		Company ghostCompany = new Company.CompanyBuilder("Non-existing company").build();
		
		when(companyService.getCompany(ghostCompany.getName())).thenReturn(Optional.empty());
		
		service.update(oldName, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(ghostCompany.getName()));
		verify(dao).update(oldName, computerWithoutCompany);
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
