package ui;

import exceptions.*;
import java.io.*;

public class CLI {

	private BufferedReader br;
	private String input;

	public static void main(String[] args) {
		CLI interpreter = new CLI();
		try {
			interpreter.getInput();
			interpreter.processInput();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IncorrectCommandException e) {
			e.printStackTrace();
		} catch (IncorrectArgumentException e) {
			e.printStackTrace();
		}
		
	}
	
	public CLI() {
		this.br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void getInput() throws IOException {
		System.out.println("Enter command: ");
		this.input = this.br.readLine();
	}
	
	/*
	 * Legal commands :
	 * 	list computers, list companies
	 * 	delete computer [computer name]
	 * 	show computer details [computer name]
	 */
	public void processInput() throws IncorrectCommandException, IncorrectArgumentException {
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
		default:
			throw new IncorrectCommandException();
		}
		
	}
	
	private void processCommandList() throws IncorrectArgumentException {
		if (this.input.indexOf(' ') == -1) {
			throw new IncorrectArgumentException();
		}
		
		String argument1 = this.input.substring(this.input.indexOf(' ')).trim().toLowerCase();
		
		switch(argument1) {
		case ("computers"):
			System.out.println("List\nof\ncomputers");
			break;
		case ("companies"):
			System.out.println("List\nof\ncompanies");
			break;
		default:
			throw new IncorrectArgumentException();
		}
		
	}
	
}
