package com.excilys.mantegazza.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mantegazza.cdb.dto.ComputerDTO;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.service.Page;

@WebServlet(urlPatterns  = "/computers")
public class ListComputers extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_COMPUTER = "/WEB-INF/views/dashboard.jsp";
	public static final String COMPUTER_LIST = "computerList";
	
	public static final String ATT_PAGE = "page";
	public static final String ATT_SEARCH_TERM = "searchTerm";
	
	public static final String PARAM_PAGE_NUMBER = "page";
	public static final String PARAM_ITEMS_PER_PAGE = "itemsPerPage";
	public static final String PARAM_ITEMS_TO_DELETE = "cb";
	public static final String PARAM_SEARCH = "search";
	public static final String PARAM_ORDER = "orderBy";

	private ComputerService computerService = new ComputerService();
	private Page page;
	private Logger logger = LoggerFactory.getLogger(ListComputers.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initPageAttribute(request);
		processParameters(request);
		fetchComputers(request);
		
		this.getServletContext().getRequestDispatcher(VIEW_COMPUTER).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameterMap().containsKey(PARAM_ITEMS_TO_DELETE)) {
			deleteComputers(request);
		}
		
		this.getServletContext().getRequestDispatcher(VIEW_COMPUTER).forward(request, response);
	}
	
	
	private void fetchComputers(HttpServletRequest request) {
		page.refreshPage();
		ArrayList<ComputerDTO> computerArray = page.getCurrentPage();
		request.setAttribute(COMPUTER_LIST, computerArray);
	}
	
	private void initPageAttribute(HttpServletRequest request) {
		HttpSession session = request.getSession();
		page = (Page) session.getAttribute(ATT_PAGE);
		
		if (page == null) {
			page = new Page();
			session.setAttribute(ATT_PAGE, page);
			logger.debug("Set session attribute ({}, Page)", ATT_PAGE);
		}
	}
	
	private void processParameters(HttpServletRequest request) {
		if (request.getParameterMap().containsKey(PARAM_SEARCH)) {
			setSearch(request);
		}
		
		if (request.getParameterMap().containsKey(PARAM_ORDER)) {
			orderSelection(request);
		}
		
		if (request.getParameterMap().containsKey(PARAM_ITEMS_PER_PAGE)) {
			page.setItemsPerPage(Integer.parseInt(request.getParameter(PARAM_ITEMS_PER_PAGE)));
		}
		
		if (request.getParameterMap().containsKey(PARAM_PAGE_NUMBER)) {
			int pageNumberParam = Integer.parseInt(request.getParameter(PARAM_PAGE_NUMBER));
			page.setToPage(pageNumberParam);
		}
		
	}
	
	private void setSearch(HttpServletRequest request) {
		String searchParam = request.getParameter(PARAM_SEARCH);
		request.getSession().setAttribute(ATT_SEARCH_TERM, searchParam);
		logger.debug("Set session attribute ({}, {})", ATT_SEARCH_TERM, searchParam);
		
		page.setSearchTerm(searchParam);
	}
		
	private void deleteComputers(HttpServletRequest request) {
		String[] checkboxesIds = request.getParameterValues(PARAM_ITEMS_TO_DELETE);
		ArrayList<Long> idsToDelete = new ArrayList<Long>();
		for (String checkboxId : checkboxesIds) {
			idsToDelete.add(Long.parseLong(checkboxId));
		}
		
		computerService.delete(idsToDelete);
	}
	
	private void orderSelection(HttpServletRequest request) {
		page.setOrderBy(request.getParameter(PARAM_ORDER));
	}
	
}
