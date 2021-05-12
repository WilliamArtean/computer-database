package service;

import ui.CLI;
import ui.Page;

import java.sql.*;
import java.text.ParseException;
import java.io.IOException;

import exceptions.*;
import persistence.CompanyDAO;
import persistence.ComputerDAO;

public class Runner {

	public static void main(String[] args) {
		
		Connection co = null;
		CLI cli;
		CompanyDAO companyDAO;
		ComputerDAO computerDAO;
		
		try {
			String url = "jdbc:mysql://localhost:3306/computer-database-db";
			String user = "admincdb";
			String pswd = "qwerty1234";
			co = DriverManager.getConnection(url, user, pswd);
			
			companyDAO = new CompanyDAO();
			computerDAO = new ComputerDAO();
			computerDAO.setCompanyDAO(companyDAO);
			companyDAO.setConnection(co);
			computerDAO.setConnection(co);
			
			Page page = new Page();
			page.fillCompanyList(companyDAO.getAll());
			while (page.nextPage()) {
				System.out.println(page.showCurrentPage());
			}
			page.firstPage();
			System.out.println(page.showCurrentPage());
			System.out.println(page.hasNextPage());
			page.lastPage();
			System.out.println(page.showCurrentPage());
			System.out.println(page.hasNextPage());
			
			cli = new CLI();
			cli.setCompanyDAO(companyDAO);
			cli.setComputerDAO(computerDAO);
			
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
		} catch (InconsistentDatesException e) {
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
