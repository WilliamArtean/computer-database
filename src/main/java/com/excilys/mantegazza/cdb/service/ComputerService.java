package com.excilys.mantegazza.cdb.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mantegazza.cdb.exceptions.InconsistentDatesException;
import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.model.Computer.ComputerBuilder;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;

import validator.DatesConsistencyValidator;

public class ComputerService {
	
	private ComputerDAO dao;
	private CompanyService companyService;
	private Logger logger = LoggerFactory.getLogger(ComputerService.class);
	private DatesConsistencyValidator dateValidator = new DatesConsistencyValidator();
	
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
	 * Creates a Computer object and inserts it into the database.
	 * @param computerName The name of the computer to create (required)
	 * @param introduced An Optional containing the introduced date
	 * @param discontinued An Optional containing the discontinued date
	 * @param companyName An Optional containing the name of the company
	 * @throws InconsistentDatesException
	 */
	public void create(String computerName, Optional<LocalDate> introduced, Optional<LocalDate> discontinued, Optional<String> companyName) throws InconsistentDatesException {
		if (!areDatesConsistent(introduced, discontinued)) {
			logger.error("Dates in computer {} are inconsistent. Introduced: {}, discontinued: {}", computerName, introduced, discontinued);
			throw new InconsistentDatesException();
		}
		
		ComputerBuilder builder = new Computer.ComputerBuilder(computerName);
		if (introduced.isPresent()) {
			builder.withIntroduced(introduced.get());
		}
		if (discontinued.isPresent()) {
			builder.withDiscontinued(discontinued.get());
		}
		if (companyName.isPresent()) {
			Optional<Company> companyToAdd = companyService.getCompany(companyName.get());
			if (companyToAdd.isPresent()) {
				builder.withCompany(companyToAdd.get());
			}
		}
		Computer computerToCreate = builder.build();
		
		dao.create(computerToCreate);
	}
	
	/**
	 * Updates a computer in the database.
	 * @param oldName The name of the computer to update (required)
	 * @param newComputerName An Optional containing the new computer name.
	 * If null, the new Computer object with have the previous name instead
	 * @param introduced An Optional containing the new introduced date
	 * @param discontinued An Optional containing the new discontinued date
	 * @param companyName An Optional containing the new name of the company
	 * @throws InconsistentDatesException 
	 */
	public void update(String oldName, Optional<String> newComputerName, Optional<LocalDate> introduced, Optional<LocalDate> discontinued, Optional<String> companyName) throws InconsistentDatesException {
		String computerName;
		if (newComputerName.isPresent()) {
			computerName = newComputerName.get();
		} else {
			computerName = oldName;
		}
		if (!areDatesConsistent(introduced, discontinued)) {
			logger.error("Dates in computer {} are inconsistent. Introduced: {}, discontinued: {}", computerName, introduced, discontinued);
			throw new InconsistentDatesException();
		}
		
		ComputerBuilder builder = new Computer.ComputerBuilder(computerName);
		if (introduced.isPresent()) {
			builder.withIntroduced(introduced.get());
		}
		if (discontinued.isPresent()) {
			builder.withDiscontinued(discontinued.get());
		}
		if (companyName.isPresent()) {
			Optional<Company> companyToAdd = companyService.getCompany(companyName.get());
			if (companyToAdd.isPresent()) {
				builder.withCompany(companyToAdd.get());
			}
		}
		Computer newComputer = builder.build();
		
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
	
	/**
	 * Checks if the introduced date is before the discontinued date.
	 * @param introduced An Optional containing the introduced date
	 * @param discontinued An Optional containing the discontinued date
	 * @return true if the discontinued date is after the introduced date, or if at least one of the dates is null.
	 * false if the discontinued date is after the introduced date.
	 */
	public boolean areDatesConsistent(Optional<LocalDate> introduced, Optional<LocalDate> discontinued) {
		if (introduced.isEmpty() || discontinued.isEmpty()) {
			return true;
		}
		return introduced.get().isBefore(discontinued.get());
	}
}
