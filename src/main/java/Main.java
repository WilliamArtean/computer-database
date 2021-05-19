import java.io.IOException;

import com.excilys.mantegazza.cdb.controller.CLIController;
import com.excilys.mantegazza.cdb.persistence.CompanyDAO;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;
import com.excilys.mantegazza.cdb.persistence.DBConnectionManager;
import com.excilys.mantegazza.cdb.service.CompanyService;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.ui.CLIView;



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
