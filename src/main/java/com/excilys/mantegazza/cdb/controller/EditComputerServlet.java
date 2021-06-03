package com.excilys.mantegazza.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
	
	
	public static final String SERVLET_LIST_COMPUTERS = "computers";
	public static final String VIEW_EDIT_COMPUTER = "/WEB-INF/views/editComputer.jsp";
	
	public static final String PARAM_ID_COMPUTER_TO_EDIT = "id";
	public static final String PARAM_COMPUTER_NAME = "computerName";
	public static final String PARAM_INTRODUCED = "introduced";
	public static final String PARAM_DISCONTINUED = "discontinued";
	public static final String PARAM_COMPANYID = "companyId";
	
	public static final String ATT_COMPUTER_TO_EDIT = "computerToEdit";
	public static final String ATT_COMPANY_LIST = "companies";
	public static final String ATT_PAGE_CONTROLLER = "webPageController";
	
	public static final String ERRORS = "errors";
	
	private String currentComputerName;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<CompanyDTO> companies = companyMapper.companiesToDTOArray(companyService.getAllCompanies());
		request.setAttribute(ATT_COMPANY_LIST, companies);
		
		if (request.getParameterMap().containsKey(PARAM_ID_COMPUTER_TO_EDIT)) {
			long id = Long.parseLong(request.getParameter(PARAM_ID_COMPUTER_TO_EDIT));
			Optional<Computer> computerToEdit = computerService.getComputer(id);
			
			if (computerToEdit.isPresent()) {
				ComputerDTO dtoToEdit = computerMapper.computerToDTO(computerToEdit.get());
				request.setAttribute(ATT_COMPUTER_TO_EDIT, dtoToEdit);
				currentComputerName = dtoToEdit.getName();
				
				this.getServletContext().getRequestDispatcher(VIEW_EDIT_COMPUTER).forward(request, response);
			} else {
				//Redirect to "computer not found" page?
			}
		} else {
			response.sendRedirect(SERVLET_LIST_COMPUTERS);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long companyId = Long.parseLong(request.getParameter(PARAM_COMPANYID));
		String computerName = request.getParameter(PARAM_COMPUTER_NAME);
		String introduced = request.getParameter(PARAM_INTRODUCED);
		String discontinued = request.getParameter(PARAM_DISCONTINUED);
		ComputerDTO computerDTOToUpdate = computerMapper.createComputerDTO(computerName, "", introduced, discontinued, companyId);
		
		HashMap<String, String> errors = computerValidator.validateComputer(computerDTOToUpdate);
		request.setAttribute(ERRORS, errors);
		
		if (errors.isEmpty()) {
			Computer newComputer = computerMapper.dtoToComputer(computerDTOToUpdate);
			computerService.update(currentComputerName, newComputer);
			
			WebPageController pageController = (WebPageController) request.getSession().getAttribute(ATT_PAGE_CONTROLLER);
			if (pageController != null) {
				pageController.refreshPage();
			}
		}
		
		doGet(request, response);
	}

}
