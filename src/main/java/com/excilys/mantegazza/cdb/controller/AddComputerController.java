package com.excilys.mantegazza.cdb.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.excilys.mantegazza.cdb.dto.CompanyDto;
import com.excilys.mantegazza.cdb.dto.ComputerDto;
import com.excilys.mantegazza.cdb.dto.mappers.CompanyDTOMapper;
import com.excilys.mantegazza.cdb.dto.mappers.ComputerDTOMapper;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.service.CompanyService;
import com.excilys.mantegazza.cdb.service.ComputerService;
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
	public ModelAndView getCompaniesInfo(Model model) {
		ModelAndView modelAndView = new ModelAndView(VIEW_ADD_COMPUTER, "computerDto", new ComputerDto());
		
		ArrayList<CompanyDto> companies = companyMapper.companiesToDTOArray(companyService.getAllCompanies());
		modelAndView.addObject(ATT_COMPANY_LIST, companies);

		return modelAndView;
	}
	
	@PostMapping("/addComputer")
	public RedirectView createComputer(@ModelAttribute("computerDto") ComputerDto computerDto,
			/*BindingResult result,*/
			Model model) {
		/*
		if (result.hasErrors()) {
			model.addAttribute(ERRORS, result.getAllErrors());
			return VIEW_ADD_COMPUTER;
		}*/
		Computer computerToCreate = computerMapper.dtoToComputer(computerDto);
		computerService.create(computerToCreate);
		
		return new RedirectView(CTRL_COMPUTER, true);
	}
	
}
