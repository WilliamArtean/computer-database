package com.excilys.mantegazza.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.mantegazza.cdb.dto.CompanyDTO;
import com.excilys.mantegazza.cdb.dto.ComputerDTO;
import com.excilys.mantegazza.cdb.dto.mappers.CompanyDTOMapper;
import com.excilys.mantegazza.cdb.dto.mappers.ComputerDTOMapper;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.service.CompanyService;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.validator.ComputerValidator;

/**
 * Servlet implementation class AddComputerServlet.
 */
@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CompanyService companyService = new CompanyService();
	private ComputerService computerService = new ComputerService();
	private CompanyDTOMapper companyMapper = new CompanyDTOMapper();
	private ComputerDTOMapper computerMapper = new ComputerDTOMapper();
	private ComputerValidator computerValidator = new ComputerValidator();
	
	public static final String VIEW_ADD_COMPUTER = "/WEB-INF/views/addComputer.jsp";
	public static final String PARAM_COMPUTER_NAME = "computerName";
	public static final String PARAM_INTRODUCED = "introduced";
	public static final String PARAM_DISCONTINUED = "discontinued";
	public static final String PARAM_COMPANYID = "companyId";
	public static final String ATT_COMPANY_LIST = "companies";
	public static final String ERRORS = "errors";
	
	private ArrayList<CompanyDTO> companies;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		companies = companyMapper.companiesToDTOArray(companyService.getAllCompanies());
		request.setAttribute(ATT_COMPANY_LIST, companies);
		
		this.getServletContext().getRequestDispatcher(VIEW_ADD_COMPUTER).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String companyId = request.getParameter(PARAM_COMPANYID);
		CompanyDTO companyDTOToCreate = null;
		if (!"0".equals(companyId)) {
			for (CompanyDTO company : companies) {
				if (companyId.equals(Long.toString(company.getId()))) {
					companyDTOToCreate = company;
				}
			}
		}
		
		String computerName = request.getParameter(PARAM_COMPUTER_NAME);
		String introduced = request.getParameter(PARAM_INTRODUCED);
		String discontinued = request.getParameter(PARAM_DISCONTINUED);
		ComputerDTO computerDTOToCreate = computerMapper.createComputerDTO(computerName, "", introduced, discontinued, companyDTOToCreate);
		
		Computer computerToCreate = computerMapper.dtoToComputer(computerDTOToCreate);

		HashMap<String, String> errors = computerValidator.validateComputer(computerToCreate);
		request.setAttribute(ERRORS, errors);
		
		if (errors.isEmpty()) {
			computerService.create(computerToCreate);
		}
		
		doGet(request, response);
	}

}
