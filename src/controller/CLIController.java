package controller;

import ui.CLIView;

public class CLIController {

	private CLIView view;

	public void setView(CLIView view) {
		this.view = view;
	}
	
	public void chooseMainMenuAction() {
		view.displayMainMenu();
		//TODO Add processing of user input
	}
	
}
