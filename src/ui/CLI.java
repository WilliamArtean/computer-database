package ui;

import exceptions.*;

public class CLI {

	public static void main(String[] args) {
		
	}
	
	public String getInput() {
		return null;
	}
	
	public void processInput(String input) {
		String commandFirstWord = input.substring(0, input.indexOf(" "));
		try {
			switch (commandFirstWord) {
			default:
				throw new IncorrectCommandException();
			}
		} catch (IncorrectCommandException e) {
			e.printStackTrace();
		}
		
	}
	
}
