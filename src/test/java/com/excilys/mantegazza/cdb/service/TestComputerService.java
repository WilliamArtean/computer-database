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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.excilys.mantegazza.cdb.exceptions.InconsistentDatesException;
import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;

@RunWith(MockitoJUnitRunner.class)
public class TestComputerService {

	private ComputerService service;

	@Mock
	private ComputerDAO dao;
	@Mock
	private CompanyService companyService = new CompanyService();
	private ArrayList<Computer> computerDB;
	
	@Before
	public void setUpService() {
		computerDB = new ArrayList<Computer>();
		computerDB.add(new Computer("Computer 1"));
		computerDB.add(new Computer("Computer 2"));
		computerDB.add(new Computer("Computer 3"));
		computerDB.add(new Computer("Computer 4"));
		
		
		service = new ComputerService();
		service.setComputerDAO(dao);
		service.setCompanyService(companyService);
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
		Computer computerToCreate = new Computer();
		String name = "computer 1";
		computerToCreate.setName(name);
		LocalDate introduced = LocalDate.of(2000, 10, 31);
		LocalDate discontinued = LocalDate.of(2020, 8, 9);
		Company company = new Company(1, "Company 1");
		
		
		computerToCreate.setIntroductionDate(introduced);
		computerToCreate.setDiscontinuationDate(discontinued);
		computerToCreate.setCompany(company);

		when(companyService.getCompany(company.getName())).thenReturn(Optional.of(company));
		service.create(computerToCreate.getName(),
				Optional.of(computerToCreate.getIntroductionDate()),
				Optional.of(computerToCreate.getDiscontinuationDate()),
				Optional.of(computerToCreate.getCompany().getName()));
		verify(dao).create(computerToCreate);
	}
	
}
