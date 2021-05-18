package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import service.CompanyService;
import service.ComputerService;
import ui.CLIView;
import utils.MenuInput;

public class CLIController {

	private CLIView view;
	private ComputerService computerService;
	private CompanyService companyService;
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	
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
		} while (!MenuInput.isValid(userChoice));
		processMainMenuInput(MenuInput.fromInteger(userChoice));
	}
	
	private void processMainMenuInput(MenuInput input) {
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
			break;
		}
	}
	
	private void listComputers() {
		
	}
	
	private void listCompanies() {
		
	}
	
	private void showDetails() {
		
	}
	
	private void createComputer() {
		
	}
	
	private void updateComputer() {
		
	}
	
	private void deleteComputer() {
		
	}
	
}
