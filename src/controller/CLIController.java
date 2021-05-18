package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import exceptions.InconsistentDatesException;
import model.Company;
import model.Computer;
import service.CompanyService;
import service.ComputerService;
import ui.CLIView;
import utils.MenuInput;

public class CLIController {

	private CLIView view;
	private ComputerService computerService;
	private CompanyService companyService;
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	
	public CLIController(CLIView view, ComputerService computerService, CompanyService companyService) {
		this.view = view;
		this.computerService = computerService;
		this.companyService = companyService;
	}
	
	
	public void setView(CLIView view) {
		this.view = view;
	}
	
	private String getInput() throws IOException {
		return this.br.readLine().trim();
	}
	
	public void chooseMainMenuAction() throws NumberFormatException, IOException {
		int userChoice = -1;
		do {
			view.displayMainMenu();
			userChoice = Integer.parseInt(getInput());
			if (!MenuInput.isValid(userChoice)) {
				System.out.println("Invalid command");
			}
			processMainMenuInput(MenuInput.fromInteger(userChoice));
		} while (MenuInput.fromInteger(userChoice).compareTo(MenuInput.EXIT) != 0);
	}
	
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
			System.out.println("Exiting application...");
			break;
		}
	}
	
	private void listComputers() {
		ArrayList<Computer> computerList = computerService.getAllComputers();
		ArrayList<String> computerNameList = new ArrayList<String>();
		
		if (computerList.isEmpty()) {
			computerNameList.add("No computer in the database");
		}
		for (Computer computer : computerList) {
			computerNameList.add(computer.getName());
		}
		
		view.displayList(computerNameList);
	}
	
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
	
	private void showDetails() throws IOException {
		System.out.println("Enter computer name:");
		String computerName = getInput();
		Optional<Computer> computer = computerService.getComputer(computerName);
		if (computer.isEmpty()) {
			System.out.println("No computer found with name '" + computerName + "'");
		} else {
			view.showDetails(computer.get());
		}
		
	}
	
	private void createComputer() throws IOException {
		String computerName = "";
		Optional<LocalDate> introduced = Optional.empty();
		Optional<LocalDate> discontinued = Optional.empty();
		Optional<String> companyName = Optional.empty();
		
		do {
			System.out.println("Enter computer name:");
			computerName = getInput();
			if ("".equals(computerName)) {
				System.out.println("You must enter a name for the computer.");
			}
		} while ("".equals(computerName));
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
			e.printStackTrace();
		}
	}
	
	private void updateComputer() throws IOException {
		Optional<String> newComputerName = Optional.empty();
		Optional<LocalDate> introduced = Optional.empty();
		Optional<LocalDate> discontinued = Optional.empty();
		Optional<String> companyName = Optional.empty();
		
		String computerName = "";
		do {
			System.out.println("Enter name of the computer to update:");
			computerName = getInput();
			if ("".equals(computerName)) {
				System.out.println("You must enter a name for the computer to update");
			}
		} while ("".equals(computerName));
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
			e.printStackTrace();
		}
	}
	
	private void deleteComputer() {
		
	}
	
}
