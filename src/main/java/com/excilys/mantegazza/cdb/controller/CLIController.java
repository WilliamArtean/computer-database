package com.excilys.mantegazza.cdb.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mantegazza.cdb.exceptions.InconsistentDatesException;
import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.service.CompanyService;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.ui.CLIView;
import com.excilys.mantegazza.cdb.ui.Page;
import com.excilys.mantegazza.cdb.utils.MenuInput;

public class CLIController {

	private CLIView view;
	private ComputerService computerService;
	private CompanyService companyService;
	private PageController pageController;
	private Scanner scanner;
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private Logger logger = LoggerFactory.getLogger(CLIController.class);

	/**
	 * Creates a CLIController that processes user input through CLI.
	 * @param view The view that will display the output in the CLI
	 * @param computerService The service for Computer objects the CLIController will use
	 * @param companyService The service for Company objects the CLIController will use
	 */
	public CLIController(CLIView view, ComputerService computerService, CompanyService companyService) {
		this.view = view;
		this.computerService = computerService;
		this.companyService = companyService;
		this.pageController = new PageController(new Page(), computerService);
		this.scanner = new Scanner(System.in);
	}
	
	/**
	 * Set the view that will be used by the CLIController.
	 * @param view The view that will display the output in the CLI
	 */
	public void setView(CLIView view) {
		this.view = view;
	}
	public void setScanner(Scanner sc) {
		this.scanner = sc;
	}
	
	public void setPageController(PageController pageController) {
		this.pageController = pageController;
	}

	/**
	 * Returns the user input from the CLI.
	 * @return a String containing what the user wrote in the CLI
	 * @throws IOException
	 */
	private String getInput() throws IOException {
		return scanner.nextLine();
	}
	
	/**
	 * Displays the main menu. When an action has been processed or the user input does not match any action,
	 * the menu shows up again, until the user chooses the 'Exit' action.
	 * If the user act
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void chooseMainMenuAction() throws IOException {
		MenuInput userChoice;
		do {
			view.displayMainMenu();
			userChoice = getUserOption();
			processMainMenuInput(userChoice);
		} while (!userChoice.equals(MenuInput.EXIT));
	}
	
	/**
	 * Returns the option chosen by the user.
	 * @return A MenuInput enum matching the number typed by the user
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private MenuInput getUserOption() throws IOException {
		MenuInput userChoice;
		try {
			String userInputString = getInput();
			int userInputInt = Integer.parseInt(userInputString);
			userChoice = MenuInput.fromInteger(userInputInt);
		} catch (NumberFormatException e) {
			logger.error("Unparsable user input");
			userChoice = MenuInput.fromInteger(0);
		}
		return userChoice;
	}
	
	/**
	 * Checks what action the user chose and call the corresponding method.
	 * @param input The enum matching the action chosen by the user
	 * @throws IOException
	 */
	private void processMainMenuInput(MenuInput input) throws IOException {
		switch (input) {
		case LIST_COMPUTERS:
			listComputers();
			break;
		case LIST_COMPANIES:
			listCompanies();
			break;
		case SHOW_DETAILS:
			showDetails();
			break;
		case CREATE_COMPUTER:
			createComputer();
			break;
		case UPDATE_COMPUTER:
			updateComputer();
			break;
		case DELETE_COMPUTER:
			deleteComputer();
			break;
		case EXIT:
			logger.trace("Exiting application");
			break;
		case INVALID_COMMAND:
			logger.info("Invalid user command input");
			System.out.println("Invalid command");
		}
	}
	
	/**
	 * Calls a PageController to show the computers list in a view with pagination.
	 * @throws IOException
	 */
	private void listComputers() throws IOException {
		this.pageController.startNavigation();
	}
	
	/**
	 * Shows the list of companies in the view.
	 */
	private void listCompanies() {
		ArrayList<Company> companyList = companyService.getAllCompanies();
		ArrayList<String> companyNameList = new ArrayList<String>();
		
		if (companyList.isEmpty()) {
			companyNameList.add("No company in the database");
		}
		for (Company company : companyList) {
			companyNameList.add(company.getName());
		}
		
		view.displayList(companyNameList);
	}
	
	/**
	 * Asks the user for a computer and show its details.
	 * @throws IOException
	 */
	private void showDetails() throws IOException {
		System.out.println("Enter computer name:");
		String computerName = getInput();
		Optional<Computer> computer = computerService.getComputer(computerName);
		if (computer.isEmpty()) {
			view.noComputerWithName(computerName);
		} else {
			view.showDetails(computer.get());
		}
	}
	
	/**
	 * Asks the user to create the data for a computer
	 * and asks the ComputerService to insert it into the database.
	 * @throws IOException
	 */
	private void createComputer() throws IOException {
		String computerName = "";
		Optional<LocalDate> introduced = Optional.empty();
		Optional<LocalDate> discontinued = Optional.empty();
		Optional<String> companyName = Optional.empty();
		
		do {
			System.out.println("Enter computer name:");
			computerName = getInput();
			if (computerName.isEmpty()) {
				view.noNameEnteredForComputer();
			}
		} while (computerName.isEmpty());
		System.out.println("Enter computer introduction date:");
		String introducedInput = getInput();
		System.out.println("Enter computer discontinuation date:");
		String discontinuedInput = getInput();
		System.out.println("Enter company name:");
		String companyNameInput = getInput();
		
		if (!introducedInput.isEmpty()) {
			introduced = Optional.of(LocalDate.parse(introducedInput, df));
		}
		if (!discontinuedInput.isEmpty()) {
			discontinued = Optional.of(LocalDate.parse(discontinuedInput, df));
		}
		if (!companyNameInput.isEmpty()) {
			companyName = Optional.of(companyNameInput);
		}
		
		try {
			computerService.create(computerName, introduced, discontinued, companyName);
		} catch (InconsistentDatesException e) {
			System.out.println("The dates are inconsistent.");
		}
	}
	
	/**
	 * Asks the user to update the data for a computer
	 * and asks the ComputerService to update it into the database.
	 * @throws IOException
	 */
	private void updateComputer() throws IOException {
		Optional<String> newComputerName = Optional.empty();
		Optional<LocalDate> introduced = Optional.empty();
		Optional<LocalDate> discontinued = Optional.empty();
		Optional<String> companyName = Optional.empty();
		
		String computerName = "";
		do {
			System.out.println("Enter the name of the computer to update:");
			computerName = getInput();
			if (computerName.isEmpty()) {
				view.noNameEnteredForComputer();
			}
		} while (computerName.isEmpty());
		System.out.println("Enter new computer name (press Enter to keep the previous name):");
		String newComputerNameInput = getInput();
		System.out.println("Enter computer introduction date:");
		String introducedInput = getInput();
		System.out.println("Enter computer discontinuation date:");
		String discontinuedInput = getInput();
		System.out.println("Enter company name:");
		String companyNameInput = getInput();
		
		if (!newComputerNameInput.isEmpty()) {
			newComputerName = Optional.of(newComputerNameInput);
		}
		if (!introducedInput.isEmpty()) {
			introduced = Optional.of(LocalDate.parse(introducedInput, df));
		}
		if (!discontinuedInput.isEmpty()) {
			discontinued = Optional.of(LocalDate.parse(discontinuedInput, df));
		}
		if (!companyNameInput.isEmpty()) {
			companyName = Optional.of(companyNameInput);
		}
		
		try {
			computerService.update(computerName, newComputerName, introduced, discontinued, companyName);
		} catch (InconsistentDatesException e) {
			System.out.println("The dates are inconsistent.");
		}
	}
	
	/**
	 * Asks the user to enter the name of a computer to delete
	 * and asks the ComputerService to erase it from the database.
	 * @throws IOException
	 */
	private void deleteComputer() throws IOException {
		String computerName = "";
		do {
			System.out.println("Enter the name of the computer to delete:");
			computerName = getInput();
			if (computerName.isEmpty()) {
				view.noNameEnteredForComputer();
			}
		} while (computerName.isEmpty());
		
		computerService.delete(computerName);
	}
	
}
