package com.excilys.mantegazza.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.excilys.mantegazza.cdb.dto.CompanyDTO;
import com.excilys.mantegazza.cdb.dto.ComputerDTO;
import com.excilys.mantegazza.cdb.dto.mappers.CompanyDTOMapper;
import com.excilys.mantegazza.cdb.dto.mappers.ComputerDTOMapper;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.service.CompanyService;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.validator.ComputerValidator;

@Controller
public class AddComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	@Autowired
	private CompanyDTOMapper companyMapper;
	@Autowired
	private ComputerDTOMapper computerMapper;
	@Autowired
	private ComputerValidator computerValidator;
	
	public static final String VIEW_ADD_COMPUTER = "/WEB-INF/views/addComputer.jsp";
	public static final String PARAM_COMPUTER_NAME = "computerName";
	public static final String PARAM_INTRODUCED = "introduced";
	public static final String PARAM_DISCONTINUED = "discontinued";
	public static final String PARAM_COMPANYID = "companyId";
	public static final String ATT_COMPANY_LIST = "companies";
	public static final String ERRORS = "errors";
	
	@Override
	@GetMapping("/addComputer")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<CompanyDTO> companies = companyMapper.companiesToDTOArray(companyService.getAllCompanies());
		request.setAttribute(ATT_COMPANY_LIST, companies);
		
		request.getRequestDispatcher(VIEW_ADD_COMPUTER).forward(request, response);
	}

	@Override
	@PostMapping("/addComputer")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long companyId = Long.parseLong(request.getParameter(PARAM_COMPANYID));
		String computerName = request.getParameter(PARAM_COMPUTER_NAME);
		String introduced = request.getParameter(PARAM_INTRODUCED);
		String discontinued = request.getParameter(PARAM_DISCONTINUED);
		ComputerDTO computerDTOToCreate = computerMapper.createComputerDTO(computerName, "", introduced, discontinued, companyId);

		HashMap<String, String> errors = computerValidator.validateComputer(computerDTOToCreate);
		request.setAttribute(ERRORS, errors);
		
		if (errors.isEmpty()) {
			Computer computerToCreate = computerMapper.dtoToComputer(computerDTOToCreate);
			computerService.create(computerToCreate);
		}
		
		doGet(request, response);
	}

}
