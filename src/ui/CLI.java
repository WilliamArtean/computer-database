package ui;

import exceptions.*;
import model.Company;
import model.Computer;
import persistence.CompanyDAO;
import persistence.ComputerDAO;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CLI {
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private BufferedReader br;
	private String input;
	private ComputerDAO computerDAO;
	private CompanyDAO companyDAO;
	private Page page = new Page();
	
	public CLI() {
		this.br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void setComputerDAO(ComputerDAO computerDAO) {
		this.computerDAO = computerDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
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
	public void processInput() throws IncorrectCommandException, IncorrectArgumentException, IOException, SQLException, ParseException, InconsistentDatesException {
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
		ArrayList<Computer> computers = computerDAO.getAll();
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
		ArrayList<Company> companies = companyDAO.getAll();
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
	
	private void processCommandShow(String args) throws IncorrectArgumentException, SQLException {
		if (args.isEmpty())
			throw new IncorrectArgumentException();
		
		String computerName = null;
		long computerID = 0;
		Computer computer = null;
		
		if (args.contains("id =") || args.contains("id=")) {
			String idString = args.substring(args.indexOf('=') + 1).trim();
			if (idString.isBlank())
				throw new IncorrectArgumentException();
			computerID = Long.parseLong(idString);
			
			computer = computerDAO.getByID(computerID);
		} else {
			computerName = args;
			computer = computerDAO.getByName(computerName);
		}
		
		if (computer != null) {
			StringBuilder compDetails = new StringBuilder();
			
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
	
	private void processCommandDelete(String args) throws IncorrectArgumentException, SQLException, InconsistentDatesException {
		if (args.isEmpty()) 
			throw new IncorrectArgumentException();
		
		String computerName = null;
		long computerID = 0;
		
		if (args.contains("id =") || args.contains("id=")) {
			String idString = args.substring(args.indexOf('=') + 1).trim();
			if (idString.isBlank())
				throw new IncorrectArgumentException();
			
			computerID = Long.parseLong(idString);
			
			if (computerDAO.getByID(computerID) == null) {
				System.out.println("No computer with id " + computerID);
				return;
			}
			
			computerDAO.delete(computerID);
		} else {
			computerName = args;
			
			if (computerDAO.getByName(computerName) == null) {
				System.out.println("No computer with name '" + computerName + "'");
				return;
			}
			
			computerDAO.delete(computerName);
		}
	}
	
	private void processCommandCreate() throws IncorrectArgumentException, IOException, ParseException, NumberFormatException, SQLException, InconsistentDatesException {
		System.out.println("Computer name");
		String computerName = this.br.readLine().trim();
		if (computerName.isEmpty()) throw new IncorrectArgumentException();
		
		Date introDate = null;
		System.out.println("Date of introduction (press Enter to leave blank)");
		String introDateString = this.br.readLine().trim();
		if (!introDateString.isEmpty()) introDate = df.parse(introDateString);
		
		Date discontDate = null;
		System.out.println("Date of discontinuation (press Enter to leave blank)");
		String discontDateString = this.br.readLine().trim();
		if (!discontDateString.isEmpty()) discontDate = df.parse(discontDateString);
		
		System.out.println("Company name (press Enter to leave blank)");
		String companyName = this.br.readLine().trim();
		
		Computer computerToCreate = new Computer(computerName);
		if (introDate != null) computerToCreate.setIntroductionDate(introDate);
		if (discontDate != null) computerToCreate.setDiscontinuationDate(discontDate);
		if (!companyName.isEmpty()) {
			Company company = companyDAO.getByName(companyName);
			if (company != null) computerToCreate.setCompany(company);
		}
		
		computerDAO.create(computerToCreate);
		
	}
	
	private void processCommandUpdate(String args) throws IncorrectArgumentException, IOException, ParseException, SQLException, InconsistentDatesException {
		if (args.isEmpty())
			throw new IncorrectArgumentException();
		
		String computerName = null;
		long computerID = 0;
		
		if (args.contains("id =") || args.contains("id=")) {
			String idString = args.substring(args.indexOf('=') + 1).trim();
			if (idString.isBlank())
				throw new IncorrectArgumentException();
			
			computerID = Long.parseLong(idString);
			
			if (computerDAO.getByID(computerID) == null) {
				System.out.println("No computer with id '" + computerID + "'");
				return;
			}
		} else {
			computerName = args;
			
			if (computerDAO.getByName(computerName) == null) {
				System.out.println("No computer with name '" + computerID + "'");
				return;
			}
		}
		
		Computer updatedComputer = new Computer();
		
		System.out.println("New name (leave blank to make no change)");
		String updatedName = this.br.readLine().trim();
		if (!updatedName.isEmpty()) {
			updatedComputer.setName(updatedName);
		}
		
		System.out.println("New date of introduction (leave blank to make no change)");
		String introDateString = this.br.readLine().trim();
		if (!introDateString.isEmpty()) {
			Date introDate = df.parse(introDateString);
			updatedComputer.setIntroductionDate(introDate);
		}
		
		System.out.println("New date of discontinuation (leave blank to make no change)");
		String discontDateString = this.br.readLine().trim();
		if (!discontDateString.isEmpty()) {
			Date discontDate = df.parse(discontDateString);
			updatedComputer.setDiscontinuationDate(discontDate);
		}
		
		System.out.println("New company name (leave blank to make no change)");
		String companyName = this.br.readLine().trim();
		if (!companyName.isEmpty()) {
			Company company = companyDAO.getByName(companyName);
			if (company != null) updatedComputer.setCompany(company);
		}
		
		if (updatedComputer.getName() != null
				|| updatedComputer.getIntroductionDate() != null
				|| updatedComputer.getDiscontinuationDate() != null
				|| updatedComputer.getCompany() != null) {
			if (computerName != null) {
				computerDAO.update(computerName, updatedComputer);
			} else {
				computerDAO.update(computerID, updatedComputer);
			}
			
		} else {
			System.out.println("No changes made to computer " + computerName);
		}
	}
	
}
