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
	public void processInput() throws IncorrectCommandException {
		if (this.input.indexOf(' ') == -1) throw new IncorrectCommandException();
		String commandFirstWord = this.input.substring(0, input.indexOf(' '));
		
		switch (commandFirstWord) {
		default:
			throw new IncorrectCommandException();
		}
		
	}
	
}
