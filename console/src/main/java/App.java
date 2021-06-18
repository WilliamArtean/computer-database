import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.mantegazza.cdb.config.AppConfig;
import com.excilys.mantegazza.cdb.controllers.CLIController;


public class App {
	public static void main(String... args) {
		ApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);
		
		CLIController cliController = appContext.getBean(CLIController.class);
		try {
			cliController.chooseMainMenuAction();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
