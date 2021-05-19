package com.excilys.mantegazza.cdb.ui;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.excilys.mantegazza.cdb.model.Computer;

public class CLIView {
	
	/**
	 * Displays the main selection menu in the CLI.
	 */
	public void displayMainMenu() {
		System.out.println("1 - Show list of computers");
		System.out.println("2 - Show list of companies");
		System.out.println("3 - Show details of a computer");
		System.out.println("4 - Create a computer");
		System.out.println("5 - Update a computer");
		System.out.println("6 - Delete a computer");
		System.out.println("7 - Exit");
	}
	
	/**
	 * Displays a list of names in the CLI.
	 * @param list An ArrayList of String
	 */
	public void displayList(ArrayList<String> list) {
		String listToDisplay = list.stream().collect(Collectors.joining("\n"));
		System.out.println(listToDisplay);
	}

	/**
	 * Displays the details of a computer in the CLI.
	 * @param computer The computer of which to show the details
	 */
	public void showDetails(Computer computer) {		
		StringBuilder details = new StringBuilder();
		details.append("ID: ").append(computer.getID());
		details.append("\n").append("NAME: ").append(computer.getName());
		
		if (computer.getIntroductionDate() != null) {
			details.append('\n').append("INTRODUCED: ").append(computer.getIntroductionDate());
		}
		if (computer.getDiscontinuationDate() != null) {
			details.append('\n').append("DISCONTINUED: ").append(computer.getDiscontinuationDate());
		}
		if (computer.getCompany() != null) {
			details.append('\n').append("COMPANY: ").append(computer.getCompany().getName());
		}
		System.out.println(details.toString());
	}
	
}
