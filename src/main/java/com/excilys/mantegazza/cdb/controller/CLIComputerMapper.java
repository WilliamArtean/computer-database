package com.excilys.mantegazza.cdb.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.model.Computer.ComputerBuilder;
import com.excilys.mantegazza.cdb.service.CompanyService;
import com.excilys.mantegazza.cdb.service.ComputerService;

@Component
public class CLIComputerMapper {
	
	@Autowired
	private CompanyService companyService;
	private Logger logger = LoggerFactory.getLogger(ComputerService.class);
		
	public Computer cliInputToComputer(Optional<String> computerName, Optional<LocalDate> introduced, Optional<LocalDate> discontinued, Optional<String> companyName) {
		ComputerBuilder builder = new Computer.ComputerBuilder();
		if (computerName.isPresent()) {
			builder.withName(computerName.get());
		}
		if (introduced.isPresent()) {
			builder.withIntroduced(introduced.get());
		}
		if (discontinued.isPresent()) {
			builder.withDiscontinued(discontinued.get());
		}
		if (companyName.isPresent()) {
			Optional<Company> companyToAdd = companyService.getCompany(companyName.get());
			if (companyToAdd.isPresent()) {
				builder.withCompany(companyToAdd.get());
			}
		}
		Computer computerToCreate = builder.build();
		
		return computerToCreate;
	}
	
}
