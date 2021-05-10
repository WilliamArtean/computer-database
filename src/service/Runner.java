package service;

import ui.CLI;
import java.sql.*;

import java.io.IOException;

import exceptions.*;

public class Runner {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection co = DriverManager.getConnection("localhost:3306/computer-database-db", "admincdb", "qwerty1234");
		} catch (Exception e) {
			
		}
		
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
