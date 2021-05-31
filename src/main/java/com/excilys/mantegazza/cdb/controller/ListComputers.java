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
	
	public static final String COMPUTER_VIEW = "/WEB-INF/views/dashboard.jsp";
	public static final String COMPUTER_LIST = "ComputerDTOList";
	
	public static final String PAGE_CONTROLLER = "WebPageController";
	public static final String PARAM_LIMIT = "limit";
	public static final String PARAM_OFFSET = "offset";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		WebPageController pageController = new WebPageController();
		session.setAttribute(PAGE_CONTROLLER, pageController);
		
		ArrayList<ComputerDTO> computerDTOArray = pageController.getFirstPage();
		session.setAttribute(COMPUTER_LIST, computerDTOArray);
		
		this.getServletContext().getRequestDispatcher(COMPUTER_VIEW).forward(req, resp);
	}
	
}
