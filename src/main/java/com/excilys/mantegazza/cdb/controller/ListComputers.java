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

@WebServlet(urlPatterns  = "/computers")
public class ListComputers extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_COMPUTER = "/WEB-INF/views/dashboard.jsp";
	public static final String COMPUTER_LIST = "computerList";
	
	public static final String ATT_PAGE_CONTROLLER = "webPageController";
	public static final String ATT_ITEM_TO_SEARCH = "itemToSearch";
	
	public static final String PARAM_PAGE_NUMBER = "page";
	public static final String PARAM_ITEMS_PER_PAGE = "itemsPerPage";
	public static final String PARAM_ITEMS_TO_DELETE = "cb";
	public static final String PARAM_SEARCH = "search";
	public static final String PARAM_ORDER = "orderBy";

	private ComputerService computerService = new ComputerService();
	private WebPageController pageController;
	private Logger logger = LoggerFactory.getLogger(ListComputers.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setPageController(request);
		
		if (request.getParameterMap().containsKey(PARAM_ORDER)) {
			orderSelection(request);
		}
		
		if (request.getParameterMap().containsKey(PARAM_SEARCH)) {
			if (request.getParameter(PARAM_SEARCH).isEmpty()) {
				endSearch(request);
			} else {
				startSearch(request);
			}
		} else {
			Object searchAttribute = request.getSession().getAttribute(ATT_ITEM_TO_SEARCH);
			if (searchAttribute == null) {
				setComputersList(request);
			} else {
				setSearchResults(request);
			}
		}
		
		this.getServletContext().getRequestDispatcher(VIEW_COMPUTER).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameterMap().containsKey(PARAM_ITEMS_TO_DELETE)) {
			deleteComputers(request);
		}
		
		this.getServletContext().getRequestDispatcher(VIEW_COMPUTER).forward(request, response);
	}
	
	
	private void setPageController(HttpServletRequest request) {
		HttpSession session = request.getSession();
		pageController = (WebPageController) session.getAttribute(ATT_PAGE_CONTROLLER);
		
		if (pageController == null) {
			pageController = new WebPageController();
			session.setAttribute(ATT_PAGE_CONTROLLER, pageController);
		}
		
		if (request.getParameterMap().containsKey(PARAM_ITEMS_PER_PAGE)) {
			pageController.setItemsPerPage(Integer.parseInt(request.getParameter(PARAM_ITEMS_PER_PAGE)));
		}
		
		if (request.getParameterMap().containsKey(PARAM_PAGE_NUMBER)) {
			int pageNumberParam = Integer.parseInt(request.getParameter(PARAM_PAGE_NUMBER));
			if (pageNumberParam >= 1 && pageNumberParam <= pageController.getNumberOfPages()) {
				pageController.setToPage(pageNumberParam);
			}
		}
	}
	
	private void setComputersList(HttpServletRequest request) {
		pageController.refreshPage();
		
		ArrayList<ComputerDTO> computerArray = pageController.getCurrentPage();
		request.setAttribute(COMPUTER_LIST, computerArray);
	}
	
	private void setSearchResults(HttpServletRequest request) {
		pageController.refreshPage();
		
		ArrayList<ComputerDTO> computerArray = pageController.getCurrentPage();
		request.setAttribute(COMPUTER_LIST, computerArray);
	}
	
	private void startSearch(HttpServletRequest request) {
		logger.info("User started search");		
		request.getSession().setAttribute(ATT_ITEM_TO_SEARCH, request.getParameter(PARAM_SEARCH));		
		logger.debug("Added attribute ({}, {})", ATT_ITEM_TO_SEARCH, request.getParameter(PARAM_SEARCH));
		
		pageController.resetOrder();
		pageController.setSearchTerm(request.getParameter(PARAM_SEARCH));
		pageController.setToPage(1);
		setSearchResults(request);
	}
	
	private void endSearch(HttpServletRequest request) {
		logger.info("User ended search");
		request.getSession().removeAttribute(ATT_ITEM_TO_SEARCH);
		logger.debug("Removed itemToSearch attribute from session");
		
		pageController.resetOrder();
		pageController.cancelSearch();
		pageController.setToPage(1);
		setComputersList(request);
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
		pageController.setOrder(request.getParameter(PARAM_ORDER));
	}
	
}
