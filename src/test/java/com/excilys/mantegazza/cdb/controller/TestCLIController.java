package com.excilys.mantegazza.cdb.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.service.CompanyService;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.ui.CLIView;

@RunWith(MockitoJUnitRunner.class)
public class TestCLIController {
	
	private static CLIController controller;
	@Mock
	private static CLIView view;
	@Mock
	private ComputerService computerService;
	@Mock
	private CompanyService companyService;
	@Mock
	private PageController pageController;
	private ArrayList<Company> companyDB;
	private ArrayList<Computer> computerDB;
	
	@Before
	public void setUpController() {
		companyDB = new ArrayList<Company>();
		Company company1 = new Company(1, "Souris");
		Company company2 = new Company(2, "Poisson");
		companyDB.add(company1);
		companyDB.add(company2);
		
		computerDB = new ArrayList<Computer>();
		Computer computer1 = new Computer("Goudgouz");
		computer1.setID(1);
		Computer computer2 = new Computer("Goudgouz Pro");
		computer2.setID(2);
		computer2.setIntroductionDate(LocalDate.of(2000, 05, 20));
		computer2.setDiscontinuationDate(LocalDate.of(2020, 12, 31));
		computer2.setCompany(company2);
		computerDB.add(computer1);
		computerDB.add(computer2);
		
		controller = new CLIController(view, computerService, companyService);
	}
	
	@After
	public void quitController() {
		
	}

	@Test
	public void testChoiceListComputers() throws IOException {
		ArrayList<String> namesList = new ArrayList<String>();
		namesList.add(computerDB.get(0).getName());
		namesList.add(computerDB.get(1).getName());
		
		//Simulating user choosing option 'list computers'
		String listComputerInput = "1" + System.lineSeparator();
		listComputerInput += "7" + System.lineSeparator();
		ByteArrayInputStream inStream = new ByteArrayInputStream(listComputerInput.getBytes());
		Scanner scanner = new Scanner(inStream);
		
		controller.setScanner(scanner);
		controller.setPageController(pageController);
		controller.chooseMainMenuAction();
		verify(pageController).startNavigation();
	}
	
	@Test
	public void testChoiceListCompanies() throws IOException {
		when(companyService.getAllCompanies()).thenReturn(companyDB);
		
		ArrayList<String> namesList = new ArrayList<String>();
		namesList.add(companyDB.get(0).getName());
		namesList.add(companyDB.get(1).getName());
		
		//Simulating user choosing option 'list companies'
		String listCompaniesInput = "2" + System.lineSeparator();
		listCompaniesInput += "7" + System.lineSeparator();
		ByteArrayInputStream inStream = new ByteArrayInputStream(listCompaniesInput.getBytes());
		Scanner scanner = new Scanner(inStream);
		
		controller.setScanner(scanner);
		controller.chooseMainMenuAction();
		verify(view).displayList(namesList);
	}

}
