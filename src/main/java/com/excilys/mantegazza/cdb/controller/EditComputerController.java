package com.excilys.mantegazza.cdb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
public class EditComputerController {

	public static final String SERVLET_LIST_COMPUTERS = "computers";
	public static final String VIEW_EDIT_COMPUTER = "editComputer";
	public static final String VIEW_COMPUTER = "dashboard";
	
	public static final String PARAM_ID_COMPUTER_TO_EDIT = "id";
	public static final String PARAM_COMPUTER_NAME = "computerName";
	public static final String PARAM_INTRODUCED = "introduced";
	public static final String PARAM_DISCONTINUED = "discontinued";
	public static final String PARAM_COMPANYID = "companyId";
	
	public static final String ATT_COMPUTER_TO_EDIT = "computerToEdit";
	public static final String ATT_COMPANY_LIST = "companies";
	public static final String ATT_PAGE_CONTROLLER = "webPageController";
	
	public static final String ERRORS = "errors";
	
	private CompanyService companyService;
	private ComputerService computerService;
	private CompanyDTOMapper companyMapper;
	private ComputerDTOMapper computerMapper;
	private ComputerValidator computerValidator;
	
	public EditComputerController(CompanyService companyService, ComputerService computerService,
			CompanyDTOMapper companyMapper, ComputerDTOMapper computerMapper, ComputerValidator computerValidator) {
		this.companyService = companyService;
		this.computerService = computerService;
		this.companyMapper = companyMapper;
		this.computerMapper = computerMapper;
		this.computerValidator = computerValidator;
	}
	
	
	@GetMapping("/editComputer")
	public ModelAndView showComputer(@RequestParam(name = PARAM_ID_COMPUTER_TO_EDIT, required = true) long id,
			ModelAndView modelAndView) {

		Optional<Computer> computerToEdit = computerService.getComputer(id);
		if (computerToEdit.isEmpty()) {
			return new ModelAndView(VIEW_COMPUTER);
		}

		ComputerDto computerDto = computerMapper.computerToDTO(computerToEdit.get());
		modelAndView = new ModelAndView(VIEW_EDIT_COMPUTER, "computerDto", computerDto);
		
		ArrayList<CompanyDto> companies = companyMapper.companiesToDTOArray(companyService.getAllCompanies());
		modelAndView.addObject(ATT_COMPANY_LIST, companies);
		
		return modelAndView;
	}
	
	@PostMapping("/editComputer")
	public RedirectView editComputer(@ModelAttribute("computerDto") ComputerDto computerDto,
			RedirectAttributes redirectAttributes
			) {
		HashMap<String, String> errors = computerValidator.validateComputer(computerDto);
		if (!errors.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERRORS, errors);
			return new RedirectView("editComputer?id=" + computerDto.getId(), true);
		}
		
		Computer newComputer = computerMapper.dtoToComputer(computerDto);
		computerService.update(computerDto.getId(), newComputer);
		
		return new RedirectView(SERVLET_LIST_COMPUTERS, true);
	}
	
}
