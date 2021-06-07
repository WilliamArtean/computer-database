package com.excilys.mantegazza.cdb.service;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;

public class ComputerService {
	
	private ComputerDAO dao = new ComputerDAO();
	private Logger logger = LoggerFactory.getLogger(ComputerService.class);
	
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
	
	public ArrayList<Computer> getComputerSelection(Page page) {
		return dao.getSelection(page);
	}
	
	/**
	 * Return the number of computers in the database.
	 * @return An int matching the number of rows in the computer database
	 */
	public int getCount() {
		return dao.getCount();
	}
	
	public int getCount(Page page) {
		return dao.getCount(page);
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
	 * @param computerId The id of the computer to update (required)
	 * @param newComputer The computer with the new data
	 * If null, the new Computer object with have the previous name instead
	 * @throws InconsistentDatesException 
	 */
	public void update(long computerId, Computer newComputer) {
		dao.update(computerId, newComputer);
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
	
	public void delete(ArrayList<Long> idsToDelete) {
		dao.delete(idsToDelete);
	}
	
}
