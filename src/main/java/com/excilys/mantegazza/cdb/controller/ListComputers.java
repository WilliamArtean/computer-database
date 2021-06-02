package com.excilys.mantegazza.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.excilys.mantegazza.cdb.dto.ComputerDTO;

@WebServlet(urlPatterns  = "/computers")
public class ListComputers extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_COMPUTER = "/WEB-INF/views/dashboard.jsp";
	public static final String COMPUTER_LIST = "computerList";
	
	public static final String ATT_PAGE_CONTROLLER = "webPageController";
	
	public static final String PARAM_PAGE_NUMBER = "page";
	public static final String PARAM_ITEMS_PER_PAGE = "itemsPerPage";
	public static final String PARAM_ITEMS_TO_DELETE = "cb";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		WebPageController pageController = (WebPageController) session.getAttribute(ATT_PAGE_CONTROLLER);
		if (pageController == null) {
			pageController = new WebPageController();
			session.setAttribute(ATT_PAGE_CONTROLLER, pageController);
		}
		
		if (req.getParameterMap().containsKey(PARAM_ITEMS_PER_PAGE)) {
			pageController.setItemsPerPage(Integer.parseInt(req.getParameter(PARAM_ITEMS_PER_PAGE)));
		}
		
		if (req.getParameterMap().containsKey(PARAM_PAGE_NUMBER)) {
			int pageNumberParam = Integer.parseInt(req.getParameter(PARAM_PAGE_NUMBER));
			if (pageNumberParam >= 1 && pageNumberParam <= pageController.getNumberOfPages()) {
				pageController.setToPage(pageNumberParam);
			}
		}
		
		ArrayList<ComputerDTO> computerArray = pageController.getCurrentPage();
		req.setAttribute(COMPUTER_LIST, computerArray);
		
		this.getServletContext().getRequestDispatcher(VIEW_COMPUTER).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] checkboxesIds = request.getParameterValues(PARAM_ITEMS_TO_DELETE);
		ArrayList<String> idsToDelete = new ArrayList<String>();
		Collections.addAll(idsToDelete, checkboxesIds);
		
		for (String id : idsToDelete) {
			System.out.println(id);
		}
		
		doGet(request, response);
	}
	
	
	
}
