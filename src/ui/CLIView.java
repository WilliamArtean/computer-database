package ui;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import model.Computer;

public class CLIView {
	
	public void displayMainMenu() {
		System.out.println("1 - Show list of computers");
		System.out.println("2 - Show list of companies");
		System.out.println("3 - Show details of a computer");
		System.out.println("4 - Create a computer");
		System.out.println("5 - Update a computer");
		System.out.println("6 - Delete a computer");
		System.out.println("7 - Exit");
	}
	
	public void displayList(ArrayList<String> list) {
		String listToDisplay = list.stream().collect(Collectors.joining("\n"));
		System.out.println(listToDisplay);
	}

	public void showDetails(Optional<Computer> computer) {
		if (computer.isEmpty()) {
			System.out.println("No such computer found");
		}
		
		StringBuilder details = new StringBuilder();
		details.append("ID: ").append(computer.get().getID());
		details.append("\n").append("NAME: ").append(computer.get().getName());
		
		if (computer.get().getIntroductionDate() != null) {
			details.append('\n').append("INTRODUCTION DATE: ").append(computer.get().getIntroductionDate());
		}
		if (computer.get().getDiscontinuationDate() != null) {
			details.append('\n').append("DISCONTINUATION DATE: ").append(computer.get().getDiscontinuationDate());
		}
		if (computer.get().getCompany() != null) {
			details.append('\n').append("COMPANY: ").append(computer.get().getCompany().getName());
		}
	}
	
}
