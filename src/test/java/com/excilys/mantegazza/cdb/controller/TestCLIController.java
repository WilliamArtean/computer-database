package com.excilys.mantegazza.cdb.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.excilys.mantegazza.cdb.exceptions.InconsistentDatesException;
import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.service.CompanyService;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.ui.CLIView;
import com.excilys.mantegazza.cdb.utils.MenuInput;

@RunWith(MockitoJUnitRunner.class)
public class TestCLIController {
	
	private static final String INVALID_COMMAND = ((Integer) MenuInput.INVALID_COMMAND.getNumber()).toString();
	private static final String LIST_COMPUTERS = ((Integer) MenuInput.LIST_COMPUTERS.getNumber()).toString();
	private static final String LIST_COMPANIES = ((Integer) MenuInput.LIST_COMPANIES.getNumber()).toString();
	private static final String SHOW_DETAILS = ((Integer) MenuInput.SHOW_DETAILS.getNumber()).toString();
	private static final String CREATE_COMPUTER = ((Integer) MenuInput.CREATE_COMPUTER.getNumber()).toString();
	private static final String UPDATE_COMPUTER = ((Integer) MenuInput.UPDATE_COMPUTER.getNumber()).toString();
	private static final String DELETE_COMPUTER = ((Integer) MenuInput.DELETE_COMPUTER.getNumber()).toString();
	private static final String EXIT = ((Integer) MenuInput.EXIT.getNumber()).toString();
	
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
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Before
	public void setUpController() {
		companyDB = new ArrayList<Company>();
		Company company1 = new Company(1, "Company 1");
		Company company2 = new Company(2, "Company 2");
		companyDB.add(company1);
		companyDB.add(company2);
		
		computerDB = new ArrayList<Computer>();
		Computer computer1 = new Computer("Computer 1");
		computer1.setID(1);
		Computer computer2 = new Computer("Computer 1");
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
		StringBuilder listComputerInput = new StringBuilder(LIST_COMPUTERS).append(System.lineSeparator());
		listComputerInput.append(EXIT).append(System.lineSeparator());
		ByteArrayInputStream inStream = new ByteArrayInputStream(listComputerInput.toString().getBytes());
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
		StringBuilder listCompaniesInput = new StringBuilder(LIST_COMPANIES).append(System.lineSeparator());
		listCompaniesInput.append(EXIT).append(System.lineSeparator());
		ByteArrayInputStream inStream = new ByteArrayInputStream(listCompaniesInput.toString().getBytes());
		Scanner scanner = new Scanner(inStream);
		
		controller.setScanner(scanner);
		controller.chooseMainMenuAction();
		verify(view).displayList(namesList);
	}
	
	@Test
	public void testShowComputerDetails() throws IOException {
		when(computerService.getComputer(computerDB.get(1).getName())).thenReturn(Optional.of(computerDB.get(1)));
		
		//Simulating user choosing option 'show details', computer name, and exit
		StringBuilder showDetailsInput = new StringBuilder(SHOW_DETAILS).append(System.lineSeparator());
		showDetailsInput.append(computerDB.get(1).getName()).append(System.lineSeparator());
		showDetailsInput.append(EXIT).append(System.lineSeparator());
		ByteArrayInputStream inStream = new ByteArrayInputStream(showDetailsInput.toString().getBytes());
		Scanner scanner = new Scanner(inStream);
		
		controller.setScanner(scanner);
		controller.chooseMainMenuAction();
		verify(view).showDetails(computerDB.get(1));
	}
	
	@Test
	public void testShowNotPresentComputerDetails() throws IOException {
		String wrongComputerName = "Non-existing computer";
		when(computerService.getComputer(wrongComputerName)).thenReturn(Optional.empty());
		
		//Simulating user choosing option 'show details', computer name, and exit
		StringBuilder showDetailsInput = new StringBuilder(SHOW_DETAILS).append(System.lineSeparator());
		showDetailsInput.append(wrongComputerName).append(System.lineSeparator());
		showDetailsInput.append(EXIT).append(System.lineSeparator());
		ByteArrayInputStream inStream = new ByteArrayInputStream(showDetailsInput.toString().getBytes());
		Scanner scanner = new Scanner(inStream);
		
		controller.setScanner(scanner);
		controller.chooseMainMenuAction();
		verify(view).noComputerWithName(wrongComputerName);
	}
	
	@Test
	public void testCreateComputer() throws IOException, InconsistentDatesException {
		String name = "Computer 3";
		String introducedString = "2001-01-02";
		LocalDate introduced = LocalDate.parse(introducedString, df);
		String discontinuedString = "2011-03-18";
		LocalDate discontinued = LocalDate.parse(discontinuedString, df);
		String company = "Company 1";
		
		StringBuilder createComputerInput = new StringBuilder(CREATE_COMPUTER).append(System.lineSeparator());
		createComputerInput.append(name).append(System.lineSeparator());
		createComputerInput.append(introducedString).append(System.lineSeparator());
		createComputerInput.append(discontinuedString).append(System.lineSeparator());
		createComputerInput.append(company).append(System.lineSeparator());
		createComputerInput.append(EXIT).append(System.lineSeparator());
		ByteArrayInputStream inStream = new ByteArrayInputStream(createComputerInput.toString().getBytes());
		Scanner scanner = new Scanner(inStream);
		
		controller.setScanner(scanner);
		controller.chooseMainMenuAction();
		verify(computerService).create(name, Optional.of(introduced), Optional.of(discontinued), Optional.of(company));
	}
	
	@Test
	public void testCreateEmptyComputer() throws IOException, InconsistentDatesException {
		String name = "Computer empty";
		
		StringBuilder createComputerInput = new StringBuilder(CREATE_COMPUTER).append(System.lineSeparator());
		createComputerInput.append(name).append(System.lineSeparator());
		createComputerInput.append(System.lineSeparator());
		createComputerInput.append(System.lineSeparator());
		createComputerInput.append(System.lineSeparator());
		createComputerInput.append(EXIT).append(System.lineSeparator());
		ByteArrayInputStream inStream = new ByteArrayInputStream(createComputerInput.toString().getBytes());
		Scanner scanner = new Scanner(inStream);
		
		controller.setScanner(scanner);
		controller.chooseMainMenuAction();
		
		verify(computerService).create(name, Optional.empty(), Optional.empty(), Optional.empty());
	}
	
	@Ignore
	@Test
	public void testCreateWithWrongFields() throws IOException {
		String name = "Lost computer";
		String wrongDate = "2000-13-31";
		String otherWrongDate = "hello there";
		
		StringBuilder createComputerInput = new StringBuilder(CREATE_COMPUTER).append(System.lineSeparator());
		createComputerInput.append(System.lineSeparator());
		createComputerInput.append(name).append(System.lineSeparator());
		createComputerInput.append(wrongDate).append(System.lineSeparator());
		createComputerInput.append(System.lineSeparator());
		createComputerInput.append(otherWrongDate).append(System.lineSeparator());
		createComputerInput.append(System.lineSeparator());
		createComputerInput.append(System.lineSeparator());
		createComputerInput.append(EXIT).append(System.lineSeparator());
		ByteArrayInputStream inStream = new ByteArrayInputStream(createComputerInput.toString().getBytes());
		Scanner scanner = new Scanner(inStream);
		
		controller.setScanner(scanner);
		controller.chooseMainMenuAction();
		
		verify(view).noNameEnteredForComputer();
		verify(view, times(2)).invalidDateEntered();
	}

}
