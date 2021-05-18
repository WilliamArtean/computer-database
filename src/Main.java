import java.io.IOException;

import controller.CLIController;
import persistence.CompanyDAO;
import persistence.ComputerDAO;
import persistence.DBConnectionManager;
import service.CompanyService;
import service.ComputerService;
import ui.CLIView;



public class Main {

public static void main(String[] args) {
		
		CompanyDAO companyDAO = new CompanyDAO();
		ComputerDAO computerDAO = new ComputerDAO();
		DBConnectionManager dbConnectionManager = new DBConnectionManager();
		companyDAO.setDatabaseManager(dbConnectionManager);
		computerDAO.setDatabaseManager(dbConnectionManager);
		
		CompanyService companyService = new CompanyService();
		companyService.setCompanyDAO(companyDAO);
		ComputerService computerService = new ComputerService();
		computerService.setComputerDAO(computerDAO);
		computerService.setCompanyService(companyService);
		
		CLIController controller = new CLIController(new CLIView(), computerService, companyService);
		try {
			controller.chooseMainMenuAction();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
