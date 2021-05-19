package com.excilys.mantegazza.cdb.controller;

import org.junit.Before;
import org.mockito.Mock;

import com.excilys.mantegazza.cdb.service.CompanyService;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.ui.CLIView;

public class TestCLIController {
	
	private CLIController controller;
	@Mock
	private CLIView view;
	@Mock
	private ComputerService computerService;
	@Mock
	private CompanyService companyService;
	
	@Before
	private void setUpController() {
		this.controller = new CLIController(view, computerService, companyService);
	}

}
