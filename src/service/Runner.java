package service;

import ui.CLI;
import java.sql.*;
import java.text.ParseException;
import java.io.IOException;

import exceptions.*;
import persistence.CompanyDAO;
import persistence.ComputerDAO;

public class Runner {

	public static void main(String[] args) {
		
		Connection co = null;
		CLI cli = CLI.getInstance();
		
		try {
			String url = "jdbc:mysql://localhost:3306/computer-database-db";
			String user = "admincdb";
			String pswd = "qwerty1234";
			co = DriverManager.getConnection(url, user, pswd);
			CompanyDAO.getInstance().setConnection(co);
			ComputerDAO.getInstance().setConnection(co);
			
			cli.getInput();
			cli.processInput();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IncorrectCommandException e) {
			e.printStackTrace();
		} catch (IncorrectArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			try {
				co.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
