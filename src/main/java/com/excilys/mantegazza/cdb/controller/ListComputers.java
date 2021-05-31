package com.excilys.mantegazza.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;

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
	
	public static final String COMPUTER_VIEW = "/WEB-INF/views/dashboard.jsp";
	public static final String COMPUTER_LIST = "computerList";
	
	public static final String PAGE_CONTROLLER = "webPageController";
	public static final String PARAM_PAGE_NUMBER = "page";
	public static final String PARAM_LIMIT = "limit";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		WebPageController pageController = (WebPageController) session.getAttribute(PAGE_CONTROLLER);
		if (pageController == null) {
			pageController = new WebPageController();
			session.setAttribute(PAGE_CONTROLLER, pageController);
		}
		
		int pageNumber = 1;
		if (req.getParameterMap().containsKey(PARAM_PAGE_NUMBER)) {
			int pageNumberParam = Integer.parseInt(req.getParameter(PARAM_PAGE_NUMBER));
			if (pageNumberParam >= 1 && pageNumberParam <= pageController.getNumberOfPages()) {
				pageNumber = pageNumberParam;
			}
		}
		pageController.setToPage(pageNumber);
		
		ArrayList<ComputerDTO> computerArray = pageController.getCurrentPage();
		req.setAttribute(COMPUTER_LIST, computerArray);
		
		this.getServletContext().getRequestDispatcher(COMPUTER_VIEW).forward(req, resp);
	}
	
}
