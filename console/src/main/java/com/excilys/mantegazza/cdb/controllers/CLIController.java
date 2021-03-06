package com.excilys.mantegazza.cdb.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.excilys.mantegazza.cdb.Company;
import com.excilys.mantegazza.cdb.CompanyService;
import com.excilys.mantegazza.cdb.Computer;
import com.excilys.mantegazza.cdb.ComputerService;
import com.excilys.mantegazza.cdb.enums.MenuInput;
import com.excilys.mantegazza.cdb.view.CLIView;

@Controller
public class CLIController {

	private CLIView view;
	private CLIComputerMapper computerMapper;
	private ComputerService computerService;
	private CompanyService companyService;
	private PageController pageController;
	private Scanner scanner = new Scanner(System.in);
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private Logger logger = LoggerFactory.getLogger(CLIController.class);

	
	public CLIController(CLIView view, CLIComputerMapper computerMapper, ComputerService computerService,
			CompanyService companyService, PageController pageController) {
		this.view = view;
		this.computerMapper = computerMapper;
		this.computerService = computerService;
		this.companyService = companyService;
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
	 * Asks the user to input a date with format "yyyy-MM-dd".
	 * @return An optional containing a date, or empty if the user typed an empty line
	 * @throws IOException 
	 */
	private Optional<LocalDate> inputDate() throws IOException {
		Optional<LocalDate> date = Optional.empty();
		boolean dateValid = false;
		
		while (!dateValid) {
			String inputDate = getInput();
			try {
				if (!inputDate.isBlank()) {
					LocalDate parsedDate = LocalDate.parse(inputDate, df);
					date = Optional.of(parsedDate);
				}
				dateValid = true;
			} catch (DateTimeParseException e) {
				view.invalidDateEntered();
			}
		}
		
		return date;
	}
	
	/**
	 * Displays the main menu. When an action has been processed or the user input does not match any action,
	 * the menu shows up again, until the user chooses the 'Exit' action.
	 * If the user act
	 * @throws NumberFormatException
	 * @throws IOExceptionion - if the text cannot be parsed
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
		case DELETE_COMPANY:
			deleteCompany();
			break;
		case EXIT:
			logger.trace("Exiting application");
			break;
		case INVALID_COMMAND:
			logger.info("Invalid user command input");
			view.invalidCommand();
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
		
		companyList.stream().forEach(c -> companyNameList.add(c.getName()));
		
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
		
		do {
			System.out.println("Enter computer name:");
			computerName = getInput();
			if (computerName.isEmpty()) {
				view.noNameEnteredForComputer();
			}
		} while (computerName.isEmpty());
		
		System.out.println("Enter computer introduction date:");
		Optional<LocalDate> introduced = inputDate();
		
		System.out.println("Enter computer discontinuation date:");
		Optional<LocalDate> discontinued = inputDate();
		
		Optional<String> companyName = Optional.empty();
		System.out.println("Enter company name:");
		String companyNameInput = getInput();
		if (!companyNameInput.isEmpty()) {
			companyName = Optional.of(companyNameInput);
		}
		
		Computer computerToCreate = computerMapper.cliInputToComputer(Optional.of(computerName), introduced, discontinued, companyName);
		computerService.create(computerToCreate);
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
		Optional<Computer> computerToUpdate = Optional.empty();
		do {
			System.out.println("Enter the name of the computer to update:");
			computerName = getInput();
			if (computerName.isEmpty()) {
				view.noNameEnteredForComputer();
			} else {
				computerToUpdate = computerService.getComputer(computerName);
				if (computerToUpdate.isEmpty()) {
					System.out.println("No computer with such name.");
				}
			}
		} while (computerToUpdate.isEmpty());
		
		
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
		
		Computer newComputer = computerMapper.cliInputToComputer(newComputerName, introduced, discontinued, companyName);
		
		
		computerService.update(computerToUpdate.get().getID(), newComputer);
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
	
	private void deleteCompany() throws IOException {
		System.out.println("Enter the name of the company to delete:");
		String companyName = getInput();
		if (companyName.isEmpty()) {
			System.out.println("Deletion canceled.");
			return;
		}
		
		System.out.println("Deleteing the company will delete all associated computers! Are you sure you want to continue? (y/n)");
		String confirmation = "";
		do {
			confirmation = getInput();
		} while (!"Y".equals(confirmation) && !"y".equals(confirmation) && !"N".equals(confirmation) && !"n".equals(confirmation));
		
		if ("Y".equals(confirmation) || "y".equals(confirmation)) {
			companyService.delete(companyName);
			System.out.println("Deleted company " + companyName + "!");
		} else {
			System.out.println("Deletion canceled.");
		}
	}
	
}
