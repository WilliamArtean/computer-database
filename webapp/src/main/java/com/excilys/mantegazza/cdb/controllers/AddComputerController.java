package com.excilys.mantegazza.cdb.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.excilys.mantegazza.cdb.CompanyService;
import com.excilys.mantegazza.cdb.Computer;
import com.excilys.mantegazza.cdb.ComputerService;
import com.excilys.mantegazza.cdb.dto.CompanyDto;
import com.excilys.mantegazza.cdb.dto.ComputerDto;
import com.excilys.mantegazza.cdb.mappers.CompanyDTOMapper;
import com.excilys.mantegazza.cdb.mappers.ComputerDTOMapper;
import com.excilys.mantegazza.cdb.validator.ComputerValidator;


@Controller
public class AddComputerController {
	
	public static final String VIEW_ADD_COMPUTER = "addComputer";
	public static final String CTRL_COMPUTER = "computers";
	
	public static final String PARAM_COMPUTER_NAME = "computerName";
	public static final String PARAM_INTRODUCED = "introduced";
	public static final String PARAM_DISCONTINUED = "discontinued";
	public static final String PARAM_COMPANYID = "companyId";
	public static final String ATT_COMPANY_LIST = "companies";
	public static final String ERRORS = "errors";
	
	private CompanyService companyService;
	private ComputerService computerService;
	private CompanyDTOMapper companyMapper;
	private ComputerDTOMapper computerMapper;
	private ComputerValidator computerValidator;
	
	
	public AddComputerController(CompanyService companyService, ComputerService computerService,
			CompanyDTOMapper companyMapper, ComputerDTOMapper computerMapper, ComputerValidator computerValidator) {
		this.companyService = companyService;
		this.computerService = computerService;
		this.companyMapper = companyMapper;
		this.computerMapper = computerMapper;
		this.computerValidator = computerValidator;
	}

	@GetMapping("/addComputer")
	public ModelAndView getCompaniesInfo() {
		ModelAndView modelAndView = new ModelAndView(VIEW_ADD_COMPUTER, "computerDto", new ComputerDto());
		
		ArrayList<CompanyDto> companies = companyMapper.companiesToDTOArray(companyService.getAllCompanies());
		modelAndView.addObject(ATT_COMPANY_LIST, companies);
		return modelAndView;
	}
	
	@PostMapping("/addComputer")
	public RedirectView createComputer(@ModelAttribute("computerDto") ComputerDto computerDto,
			RedirectAttributes redirectAttributes
			) {
		HashMap<String, String> errors = computerValidator.validateComputer(computerDto);
		if (!errors.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERRORS, errors);
			return new RedirectView(VIEW_ADD_COMPUTER, true);
		}
		
		Computer computerToCreate = computerMapper.dtoToComputer(computerDto);
		computerService.create(computerToCreate);
		
		return new RedirectView(CTRL_COMPUTER, true);
	}
	
}
