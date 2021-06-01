package com.excilys.mantegazza.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

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

@WebServlet("/editComputer")
public class EditComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	private CompanyService companyService = new CompanyService();
	private ComputerService computerService = new ComputerService();
	private CompanyDTOMapper companyMapper = new CompanyDTOMapper();
	private ComputerDTOMapper computerMapper = new ComputerDTOMapper();
	private ComputerValidator computerValidator = new ComputerValidator();
	
	public static final String VIEW_EDIT_COMPUTER = "/WEB-INF/views/editComputer.jsp";
	public static final String PARAM_NAME_COMPUTER_TO_EDIT = "name";
	public static final String PARAM_COMPUTER_NAME = "computerName";
	public static final String PARAM_INTRODUCED = "introduced";
	public static final String PARAM_DISCONTINUED = "discontinued";
	public static final String PARAM_COMPANYID = "companyId";
	public static final String ATT_COMPUTER_TO_EDIT = "computerToEdit";
	public static final String ATT_COMPANY_LIST = "companies";
	public static final String ERRORS = "errors";
	
	private ArrayList<CompanyDTO> companies;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		companies = companyMapper.companiesToDTOArray(companyService.getAllCompanies());
		request.setAttribute(ATT_COMPANY_LIST, companies);
		
		if (request.getParameterMap().containsKey(PARAM_NAME_COMPUTER_TO_EDIT)) {
			Optional<Computer> computerToEdit = computerService.getComputer(request.getParameter(PARAM_NAME_COMPUTER_TO_EDIT));
			if (computerToEdit.isPresent()) {
				ComputerDTO dtoToEdit = computerMapper.computerToDTO(computerToEdit.get());
				request.setAttribute(ATT_COMPUTER_TO_EDIT, dtoToEdit);
				
				this.getServletContext().getRequestDispatcher(VIEW_EDIT_COMPUTER).forward(request, response);
			} else {
				//Redirect to "computer not found" page?
			}
		} else {
			//redirect to main page
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
