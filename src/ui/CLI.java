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

	private static CLI instance = new CLI();
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private BufferedReader br;
	private String input;
	
	public CLI() {
		this.br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public static CLI getInstance() {
		return instance;
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
	public void processInput() throws IncorrectCommandException, IncorrectArgumentException, IOException, SQLException, ParseException {
		String commandFirstWord;
		if (this.input.indexOf(' ') != -1) {
			commandFirstWord = this.input.substring(0, this.input.indexOf(' ')).toLowerCase();
		} else {
			commandFirstWord = this.input;
		}
		
		switch (commandFirstWord) {
		case ("list"):
			processCommandList();
			break;
		case ("show"):
			if (this.input.length() >= "show details".length() && this.input.substring(0, 12).equals("show details")) {
				processCommandShow();
			} else {
				throw new IncorrectCommandException();
			}
			break;
		case ("delete"):
			if (this.input.length() >= "delete computer".length() && this.input.substring(0, 15).equals("delete computer")) {
				processCommandDelete();
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
				processCommandUpdate();
			} else {
				throw new IncorrectCommandException();
			}
			break;
		default:
			throw new IncorrectCommandException();
		}
		
	}
	
	private void processCommandList() throws IncorrectArgumentException, SQLException {
		if (this.input.indexOf(' ') == -1) {
			throw new IncorrectArgumentException();
		}
		
		String argument = this.input.substring(this.input.indexOf(' ') + 1).toLowerCase();
		
		switch(argument) {
		case ("computers"):
			ArrayList<Computer> computers = ComputerDAO.getInstance().getAll();
			StringBuilder computersList = new StringBuilder();
			for (Computer comp : computers) {
				computersList.append(comp.getName()).append('\n');
			}
			System.out.println(computersList);
			break;
		case ("companies"):
			ArrayList<Company> companies = CompanyDAO.getInstance().getAll();
			StringBuilder companiesList = new StringBuilder();
			for (Company comp : companies) {
				companiesList.append(comp.getName()).append('\n');
			}
			System.out.println(companiesList);
			break;
		default:
			throw new IncorrectArgumentException();
		}
	}
	
	private void processCommandShow() throws IncorrectArgumentException, SQLException {
		String computerName = this.input.substring("show details".length()).trim();
		
		if (computerName.length() == 0) throw new IncorrectArgumentException();
		
		Computer computer = ComputerDAO.getInstance().getByName(computerName);
		
		if (computer != null) {
			StringBuilder compDetails = new StringBuilder();
			compDetails.append("NAME: ").append(computer.getName());
			
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
			System.out.println("No computer with name \"" + computerName + "\" found.");
		}
	}
	
	private void processCommandDelete() throws IncorrectArgumentException, SQLException {
		String computerName = this.input.substring("delete computer".length()).trim();
		
		if (!computerName.isEmpty()) {
			Computer computerToDelete = ComputerDAO.getInstance().getByName(computerName);
			if (computerToDelete != null) {
				ComputerDAO.getInstance().delete(computerName);
			} else {
				System.out.println("No computer with name \"" + computerName + "\".");
			}
		} else {
			throw new IncorrectArgumentException();
		}
	}
	
	private void processCommandCreate() throws IncorrectArgumentException, IOException, ParseException, NumberFormatException, SQLException {
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
			Company company = CompanyDAO.getInstance().getByName(companyName);
			if (company != null) computerToCreate.setCompany(company);
		}
		
		ComputerDAO.getInstance().create(computerToCreate);
		
	}
	
	private void processCommandUpdate() throws IncorrectArgumentException, IOException, ParseException, SQLException {
		String computerName = this.input.substring("update computer".length()).trim();
		if (computerName.isEmpty()) throw new IncorrectArgumentException();
		
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
			Company company = CompanyDAO.getInstance().getByName(companyName);
			if (company != null) updatedComputer.setCompany(company);
		}
		
		if (updatedComputer.getName() != null
				|| updatedComputer.getIntroductionDate() != null
				|| updatedComputer.getDiscontinuationDate() != null
				|| updatedComputer.getCompany() != null) {
			ComputerDAO.getInstance().update(computerName, updatedComputer);
		} else {
			System.out.println("No changes made to computer " + computerName);
		}
	}
	
}
