package com.excilys.mantegazza.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.mantegazza.cdb.dto.ComputerDTO;

@WebServlet(urlPatterns  = "/computers")
public class ListComputers extends HttpServlet {
	
	public static final String COMPUTER_VIEW = "/WEB-INF/ListComputersView.jsp";
	public static final String PARAM_LIMIT = "limit";
	public static final String PARAM_OFFSET = "offset";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		this.getServletContext().getRequestDispatcher(COMPUTER_VIEW).forward(req, resp);
	}
	
}
