package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ui.CLIView;
import utils.MenuInput;

public class CLIController {

	private CLIView view;
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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
	}
	
}
