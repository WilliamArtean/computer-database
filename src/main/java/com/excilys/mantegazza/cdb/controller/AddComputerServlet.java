package com.excilys.mantegazza.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.mantegazza.cdb.dto.CompanyDTO;
import com.excilys.mantegazza.cdb.dto.mappers.CompanyDTOMapper;
import com.excilys.mantegazza.cdb.service.CompanyService;

/**
 * Servlet implementation class AddComputerServlet.
 */
@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_ADD_COMPUTER = "/WEB-INF/views/addComputer.jsp";
	
	private CompanyService companyService = new CompanyService();
	private CompanyDTOMapper companyMapper = new CompanyDTOMapper();
	
	public static final String PARAM_COMPUTER_NAME = "computerName";
	public static final String PARAM_INTRODUCED = "introduced";
	public static final String PARAM_DISCONTINUED = "discontinued";
	public static final String ATT_COMPANY_LIST = "companies";


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<CompanyDTO> companies = companyMapper.companiesToDTOArray(companyService.getAllCompanies());
		request.setAttribute(ATT_COMPANY_LIST, companies);
		
		this.getServletContext().getRequestDispatcher(VIEW_ADD_COMPUTER).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String[]> params = request.getParameterMap();
		System.out.println(params.size());
		for (String key : params.keySet()) {
			System.out.println(key);
		}
		System.out.println("Computer Name: " + request.getParameter(PARAM_COMPUTER_NAME));
		doGet(request, response);
	}

}
