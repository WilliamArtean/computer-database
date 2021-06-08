import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.mantegazza.cdb.config.AppConfig;
import com.excilys.mantegazza.cdb.controller.CLIController;
import com.excilys.mantegazza.cdb.persistence.CompanyDAO;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;
import com.excilys.mantegazza.cdb.service.CompanyService;
import com.excilys.mantegazza.cdb.service.ComputerService;

public class App {
	/*
	@Autowired
	public static CLIController controller;

	public static void main(String[] args) {
		//ApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);
		//CLIController controller = appContext.getBean(CLIController.class);
		
		Logger logger = LoggerFactory.getLogger(App.class);
		logger.trace("Started application");
		try {
			controller.chooseMainMenuAction();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
}
