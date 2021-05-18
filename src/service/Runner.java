package service;

import ui.CLIView;
import controller.CLIController;

import java.io.IOException;

import persistence.CompanyDAO;
import persistence.ComputerDAO;
import persistence.DBConnectionManager;

public class Runner {

	public static void main(String[] args) {
		
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
			
			CLIController controller = new CLIController(new CLIView(), computerService, companyService);
			controller.chooseMainMenuAction();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
