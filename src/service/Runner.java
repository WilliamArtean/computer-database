package service;

import ui.CLI;

import java.io.IOException;

import exceptions.*;

public class Runner {

	public static void main(String[] args) {
		CLI cli = CLI.getInstance();
		try {
			cli.getInput();
			cli.processInput();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IncorrectCommandException e) {
			e.printStackTrace();
		} catch (IncorrectArgumentException e) {
			e.printStackTrace();
		}
	}

}
