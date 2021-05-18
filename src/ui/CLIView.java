package ui;

import java.util.ArrayList;

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
		StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
		}
		System.out.println(sb.toString());
	}

}
