package com.excilys.mantegazza.cdb.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.model.Computer.ComputerBuilder;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;

public class ComputerService {
	
	private ComputerDAO dao = new ComputerDAO();
	private CompanyService companyService = new CompanyService();
	private Logger logger = LoggerFactory.getLogger(ComputerService.class);
	
	/**
	 * Sets the company service from which this service can get the Company objects it needs.
	 * @param companyService The CompanyService this service will use
	 */
	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	/**
	 * Set a ComputerDAO from which to get the computers from the database.
	 * @param dao The ComputerDAO to use with this ComputerService
	 */
	public void setComputerDAO(ComputerDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * Get a computer from the CompanyDAO.
	 * @param name The name of the computer to fetch
	 * @return An Optional containing a Computer object matching the name, or an empty Optional if the computer could not be found
	 */
	public Optional<Computer> getComputer(String name) {
		return dao.getByName(name);
	}
	/**
	 * Get a computer from the CompanyDAO.
	 * @param id The id of the computer to fetch
	 * @return An Optional containing a Computer object matching the name, or an empty Optional if the computer could not be found
	 */
	public Optional<Computer> getComputer(long id) {
		return dao.getByID(id);
	}

	/**
	 * Get all the computers from the ComputerDAO.
	 * @return An ArrayList of all the Computer objects in the database
	 */
	public ArrayList<Computer> getAllComputers() {
		return dao.getAll();
	}
	
	/**
	 * Get a selected number of computers from the database.
	 * @param numberToGet The number of computers to return.
	 * @param rowOffset The offset from which to start the selection. 0 to start from the first row (included)
	 * @return An ArrayList of Computer objects in the database, of size numberToReturn at most (less if the end of the database has been reached)
	 */
	public ArrayList<Computer> getComputerSelection(int numberToGet, int rowOffset) {
		return dao.getSelection(numberToGet, rowOffset);
	}
	
	/**
	 * Return the number of computers in the database.
	 * @return An int matching the number of rows in the computer database
	 */
	public int getCount() {
		return dao.getCount();
	}
	
	/**
	 * Inserts a Computer into the database.
	 * @param computerToCreate the Computer to insert
	 * @throws InconsistentDatesException
	 */
	public void create(Computer computerToCreate) {
		//TODO Validation?
		dao.create(computerToCreate);
	}
	
	/**
	 * Updates a computer in the database.
	 * @param oldName The name of the computer to update (required)
	 * @param newComputer The computer with the new data
	 * If null, the new Computer object with have the previous name instead
	 * @throws InconsistentDatesException 
	 */
	public void update(String oldName, Computer newComputer) {
		dao.update(oldName, newComputer);
	}
	
	/**
	 * Deletes a computer from the database.
	 * @param name The name of the computer to delete
	 */
	public void delete(String name) {
		dao.delete(name);
	}
	/**
	 * Deletes a computer from the database.
	 * @param id The id of the computer to delete
	 */
	public void delete(long id) {
		dao.delete(id);
	}
}
