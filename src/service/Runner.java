package service;

import ui.CLI;
import java.sql.*;

import java.io.IOException;

import exceptions.*;
import persistence.CompanyDAO;
import persistence.ComputerDAO;

public class Runner {

	public static void main(String[] args) {
		try {
			String url = "jdbc:mysql://localhost:3306/computer-database-db";
			String user = "admincdb";
			String pswd = "qwerty1234";
			Connection co = DriverManager.getConnection(url, user, pswd);
			CompanyDAO.getInstance().setConnection(co);
			ComputerDAO.getInstance().setConnection(co);
		} catch (SQLException e) {
			e.printStackTrace();
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
