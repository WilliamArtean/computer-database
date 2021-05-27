package com.excilys.mantegazza.cdb.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.mantegazza.cdb.dto.ComputerDTO;

@WebServlet(urlPatterns  = "/computers")
public class ListComputers extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ComputerDTO userComputer = new ComputerDTO();
		if (req.getParameter("name") != null) {
			userComputer.setName(req.getParameter("name"));
		}
		if (req.getParameter("id") != null) {
			userComputer.setId(Long.parseLong(req.getParameter("id")));
		}
		if (req.getParameter("companyId") != null) {
			userComputer.setCompanyId(Long.parseLong(req.getParameter("companyId")));
		}
		
		req.setAttribute("userComputer", userComputer);
		this.getServletContext().getRequestDispatcher("/WEB-INF/ListComputersView.jsp").forward(req, resp);
	}
	
}
