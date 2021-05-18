package ui;

import exceptions.*;
import model.Company;
import model.Computer;
import service.ComputerService;
import service.CompanyService;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class CLI {
	
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private BufferedReader br;
	private String input;
	private ComputerService computerService;
	private CompanyService companyService;
	private Page page = new Page();
	
	public CLI() {
		this.br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void setComputerService(ComputerService computerService) {
		this.computerService = computerService;
	}
	
	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}
	
	public void getInput() throws IOException {
		System.out.println("Enter command: ");
		this.input = this.br.readLine().trim();
	}
	
	/*
	 * Legal commands :
	 * 	list computers, list companies
	 * 	show details [computer name]
	 * 	delete computer [computer name]
	 * 	create computer
	 * 	update computer [computer name]
	 */
	/**
	 * Parses user input, analyzes the command and passes argument analysis to the appropriate method
	 * Legal commands are : "list computers (page [page number])", "list companies (page [page number])",
	 * "show details [computer name] / id=[computer id]", "create computer",
	 * "delete computer [computer name] / id=[computer id]", "update computer [computer name] / id=[computer id]"
	 * @throws IncorrectCommandException
	 * @throws IncorrectArgumentException
	 * @throws IOException
	 * @throws SQLException
	 * @throws ParseException
	 * @throws InconsistentDatesException
	 */
	public void processInput() throws IncorrectCommandException, IncorrectArgumentException, IOException, SQLException, ParseException {
		String commandFirstWord = "";
		String args = "";
		if (this.input.indexOf(' ') != -1) {
			commandFirstWord = this.input.substring(0, this.input.indexOf(' ')).toLowerCase();
		} else {
			commandFirstWord = this.input;
		}
		
		switch (commandFirstWord) {
		case ("list"):
			args = input.substring("list".length()).trim().toLowerCase();
			processCommandList(args);
			break;
		case ("show"):
			if (this.input.length() >= "show details".length() && this.input.substring(0, 12).equals("show details")) {
				args = input.substring("show details".length()).trim();
				processCommandShow(args);
			} else {
				throw new IncorrectCommandException();
			}
			break;
		case ("delete"):
			if (this.input.length() >= "delete computer".length() && this.input.substring(0, 15).equals("delete computer")) {
				args = input.substring("delete computer".length()).trim();
				processCommandDelete(args);
			} else {
				throw new IncorrectCommandException();
			}
			break;
		case ("create"):
			if (this.input.equals("create computer")) {
				processCommandCreate();
			} else {
				throw new IncorrectCommandException();
			}
			break;
		case ("update"):
			if (this.input.length() >= "update computer".length() && this.input.substring(0, "update computer".length()).equals("update computer")) {
				args = input.substring("update computer".length()).trim();
				processCommandUpdate(args);
			} else {
				throw new IncorrectCommandException();
			}
			break;
		default:
			throw new IncorrectCommandException();
		}
		
	}
	
	/**
	 * Processes the "list" command
	 * @param args A string that should either be "computers" or "companies",
	 * possibly followed by "page [page number]
	 * @throws IncorrectArgumentException
	 * @throws SQLException
	 */
	private void processCommandList(String args) throws IncorrectArgumentException, SQLException {
		if (args.isEmpty() || args.length() < "computers".length())
			throw new IncorrectArgumentException();

		String firstArg = args.substring(0, "computers".length());
		switch(firstArg) {
		case ("computers"):
			listComputers(args.substring("computers".length()).trim());
			break;
		case ("companies"):
			listCompanies(args.substring("companies".length()).trim());
			break;
		}
	}
	
	private void listComputers(String arg) throws SQLException, IncorrectArgumentException {
		ArrayList<Computer> computers = computerService.getAllComputers();
		StringBuilder computersList = new StringBuilder();
		for (Computer comp : computers) {
			computersList.append(comp.getName()).append('\n');
		}
		
		if (arg.isEmpty()) {
			System.out.println(computersList);
		} else if (arg.contains("page")) {
			page.fillComputerList(computers);
			int pageNumber = Integer.parseInt(arg.substring(5).trim());
			System.out.println(page.showPage(pageNumber));
		} else {
			throw new IncorrectArgumentException();
		}
	}
	
	private void listCompanies(String arg) throws SQLException, IncorrectArgumentException {
		ArrayList<Company> companies = companyService.getAllCompanies();
		StringBuilder companiesList = new StringBuilder();
		for (Company comp : companies) {
			companiesList.append(comp.getName()).append('\n');
		}
		
		if (arg.isEmpty()) {
			System.out.println(companiesList);
		} else if (arg.contains("page")) {
			page.fillCompanyList(companies);
			int pageNumber = Integer.parseInt(arg.substring(4).trim());
			System.out.println(page.showPage(pageNumber));
		} else {
			throw new IncorrectArgumentException();
		}
	}
	
	/**
	 * processes the "show details" command
	 * @param args A string containing the computer name or "id = [computer id]"
	 * @throws IncorrectArgumentException
	 * @throws SQLException
	 */
	private void processCommandShow(String args) throws IncorrectArgumentException, SQLException {
		if (args.isEmpty())
			throw new IncorrectArgumentException();
		
		String computerName = null;
		long computerID = 0;
		Optional<Computer> computerOpt;
		
		if (args.contains("id =") || args.contains("id=")) {
			String idString = args.substring(args.indexOf('=') + 1).trim();
			if (idString.isBlank())
				throw new IncorrectArgumentException();
			computerID = Long.parseLong(idString);
			computerOpt = computerService.getComputer(computerID);
		} else {
			computerName = args;
			computerOpt = computerService.getComputer(computerName);
		}
		
		if (computerOpt.isPresent()) {
			StringBuilder compDetails = new StringBuilder();
			
			Computer computer = computerOpt.get();
			compDetails.append("ID: ").append(computer.getID());
			compDetails.append('\n').append("NAME: ").append(computer.getName());
			
			if (computer.getIntroductionDate() != null) {
				compDetails.append('\n').append("INTRODUCTION DATE: ").append(computer.getIntroductionDate());
			}
			if (computer.getDiscontinuationDate() != null) {
				compDetails.append('\n').append("DISCONTINUATION DATE: ").append(computer.getDiscontinuationDate());
			}
			if (computer.getCompany() != null) {
				compDetails.append('\n').append("COMPANY: ").append(computer.getCompany().getName());
			}
			
			System.out.println(compDetails);
		} else {
			if (computerName == null) {
				System.out.println("No computer with id " + computerID + " found.");
			} else {
				System.out.println("No computer with name '" + computerName + "' found.");
			}
		}
	}
	
	private void processCommandDelete(String args) throws IncorrectArgumentException, SQLException {
		if (args.isEmpty()) 
			throw new IncorrectArgumentException();
		
		String computerName = null;
		long computerID = 0;
		
		if (args.contains("id =") || args.contains("id=")) {
			String idString = args.substring(args.indexOf('=') + 1).trim();
			if (idString.isBlank())
				throw new IncorrectArgumentException();
			
			computerID = Long.parseLong(idString);
			
			if (computerService.getComputer(computerID) == null) {
				System.out.println("No computer with id " + computerID);
				return;
			}
			
			computerService.delete(computerID);
		} else {
			computerName = args;
			
			if (computerService.getComputer(computerName) == null) {
				System.out.println("No computer with name '" + computerName + "'");
				return;
			}
			
			computerService.delete(computerName);
		}
	}
	
	private void processCommandCreate() throws IncorrectArgumentException, IOException, ParseException, NumberFormatException, SQLException {
		System.out.println("Computer name");
		String computerName = this.br.readLine().trim();
		if (computerName.isEmpty()) {
			throw new IncorrectArgumentException();
		}
		
		Optional<LocalDate> introDate = Optional.empty();
		System.out.println("Date of introduction (press Enter to leave blank)");
		String introDateString = this.br.readLine().trim();
		if (!introDateString.isEmpty()) {
			introDate = Optional.of(LocalDate.parse(introDateString, df));
		}
		
		Optional<LocalDate> discontDate = Optional.empty();
		System.out.println("Date of discontinuation (press Enter to leave blank)");
		String discontDateString = this.br.readLine().trim();
		if (!discontDateString.isEmpty()) {
			discontDate = Optional.of(LocalDate.parse(discontDateString, df));
		}
		
		Optional<String> companyName = Optional.empty();
		System.out.println("Company name (press Enter to leave blank)");
		String companyNameInput = this.br.readLine().trim();
		if (!companyNameInput.isEmpty()) {
			companyName = Optional.of(companyNameInput);
		}
		
		computerService.create(computerName, introDate, discontDate, companyName);
		
	}
	
	private void processCommandUpdate(String args) throws IncorrectArgumentException, IOException, ParseException, SQLException {
		if (args.isEmpty())
			throw new IncorrectArgumentException();
		
		String computerName = args;
		
		if (computerService.getComputer(computerName) == null) {
			System.out.println("No computer with name '" + computerName + "'");
			return;
		}
		
		Optional<String> updatedName = Optional.empty();
		System.out.println("New name (leave blank to keep previous name)");
		String updatedNameInput = this.br.readLine().trim();
		if (!updatedNameInput.isEmpty()) {
			updatedName = Optional.of(updatedNameInput);
		}
		
		
		Optional<LocalDate> introDate = Optional.empty();
		System.out.println("New date of introduction (leave blank to set to NULL)");
		String introDateInput = this.br.readLine().trim();
		if (!introDateInput.isEmpty()) {
			introDate = Optional.of(LocalDate.parse(introDateInput, df));
		}
		
		Optional<LocalDate> discontDate = Optional.empty();
		System.out.println("New date of discontinuation (leave blank to set to NULL)");
		String discontDateInput = this.br.readLine().trim();
		if (!discontDateInput.isEmpty()) {
			discontDate = Optional.of(LocalDate.parse(discontDateInput, df));
		}
		
		Optional<String> companyName = Optional.empty();
		System.out.println("New company name (leave blank to set to NULL)");
		String companyNameInput = this.br.readLine().trim();
		if (!companyNameInput.isEmpty()) {
			companyName = Optional.of(companyNameInput);
		}
		computerService.update(computerName, updatedName, introDate, discontDate, companyName);
	}
	
}
