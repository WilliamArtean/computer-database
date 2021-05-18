package service;

import ui.CLI;
import ui.CLIView;

import java.sql.*;
import java.text.ParseException;

import controller.CLIController;

import java.io.IOException;

import exceptions.*;
import persistence.CompanyDAO;
import persistence.ComputerDAO;
import persistence.DBConnectionManager;

public class Runner {

	public static void main(String[] args) {
		
		CLI cli;
		CompanyDAO companyDAO;
		ComputerDAO computerDAO;
		
		try {			
			companyDAO = new CompanyDAO();
			computerDAO = new ComputerDAO();
			DBConnectionManager dbConnectionManager = new DBConnectionManager();
			companyDAO.setDatabaseManager(dbConnectionManager);
			computerDAO.setDatabaseManager(dbConnectionManager);
			
			CompanyService companyService = new CompanyService();
			companyService.setCompanyDAO(companyDAO);
			ComputerService computerService = new ComputerService();
			computerService.setComputerDAO(computerDAO);
			computerService.setCompanyService(companyService);
			
			CLIController controller = new CLIController(new CLIView());
			controller.chooseMainMenuAction();
			
			cli = new CLI();
			cli.setComputerService(computerService);
			cli.setCompanyService(companyService);
			
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
		}
	}

}
