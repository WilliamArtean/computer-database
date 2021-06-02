import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mantegazza.cdb.controller.CLIController;
import com.excilys.mantegazza.cdb.persistence.CompanyDAO;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;
import com.excilys.mantegazza.cdb.service.CompanyService;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.ui.CLIView;



public class Main {

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.trace("Started application");
		
		CompanyDAO companyDAO = new CompanyDAO();
		ComputerDAO computerDAO = new ComputerDAO();
		
		CompanyService companyService = new CompanyService();
		companyService.setCompanyDAO(companyDAO);
		ComputerService computerService = new ComputerService();
		computerService.setComputerDAO(computerDAO);
		
		CLIController controller = new CLIController(new CLIView(), computerService, companyService);
		try {
			controller.chooseMainMenuAction();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
