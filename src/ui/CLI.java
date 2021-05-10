package ui;

import exceptions.*;
import model.Company;
import persistence.CompanyDAO;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class CLI {

	private static CLI instance = new CLI();
	
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
	public void processInput() throws IncorrectCommandException, IncorrectArgumentException, IOException, SQLException {
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
		
		String argument1 = this.input.substring(this.input.indexOf(' ') + 1).toLowerCase();
		
		switch(argument1) {
		case ("computers"):
			System.out.println("List\nof\ncomputers");
			break;
		case ("companies"):
			ArrayList<Company> companies = CompanyDAO.getInstance().getAll();
			StringBuilder sb = new StringBuilder();
			for (Company comp : companies) {
				sb.append(comp.getName()).append('\n');
			}
			System.out.println(sb);
			break;
		default:
			throw new IncorrectArgumentException();
		}
	}
	
	private void processCommandShow() throws IncorrectArgumentException {
		String arguments = this.input.substring("show details".length()).trim();
		
		if (arguments.length() > 0) {
			System.out.println("Computer details\n\tName: " + arguments + "\n\tDetailsblablabla");
		} else {
			throw new IncorrectArgumentException();
		}
	}
	
	private void processCommandDelete() throws IncorrectArgumentException {
		String arguments = this.input.substring("delete computer".length()).trim();
		
		if (arguments.length() > 0) {
			System.out.println("Computer " + arguments + " deleted");
		} else {
			throw new IncorrectArgumentException();
		}
	}
	
	private void processCommandCreate() throws IncorrectArgumentException, IOException {
		System.out.println("Computer name");
		String computerName = this.br.readLine().trim();
		if (computerName.isEmpty()) throw new IncorrectArgumentException();
		
		System.out.println("Date of introduction (press Enter to leave blank)");
		String introDate = this.br.readLine().trim();
		
		System.out.println("Date of discontinuation (press Enter to leave blank)");
		String discontDate = this.br.readLine().trim();
		
		System.out.println("Company name (press Enter to leave blank)");
		String companyName = this.br.readLine().trim();
		
		StringBuilder sb = new StringBuilder("Computer created!\n\tName: ").append(computerName);
		if (!introDate.isEmpty()) sb.append("\n\tIntroduction date: ").append(introDate);
		if (!discontDate.isEmpty()) sb.append("\n\tDiscontinuation date: ").append(discontDate);
		if (!companyName.isEmpty()) sb.append("\n\tCompany: ").append(companyName);
		
		System.out.println(sb);
	}
	
	private void processCommandUpdate() throws IncorrectArgumentException, IOException {
		String argument = this.input.substring("update computer".length()).trim();
		if (argument.isEmpty()) throw new IncorrectArgumentException();
		
		System.out.println("Date of introduction (leave blank to make no change)");
		String introDate = this.br.readLine().trim();
		
		System.out.println("Date of discontinuation (leave blank to make no change)");
		String discontDate = this.br.readLine().trim();
		
		System.out.println("Company name (leave blank to make no change)");
		String companyName = this.br.readLine().trim();
		
		StringBuilder sb = new StringBuilder("Computer updated!\n\tName: ").append(argument);
		if (!introDate.isEmpty()) sb.append("\n\tIntroduction date: ").append(introDate);
		if (!discontDate.isEmpty()) sb.append("\n\tDiscontinuation date: ").append(discontDate);
		if (!companyName.isEmpty()) sb.append("\n\tCompany: ").append(companyName);
		
		System.out.println(sb);
	}
	
}
