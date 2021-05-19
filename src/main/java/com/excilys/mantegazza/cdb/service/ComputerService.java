package com.excilys.mantegazza.cdb.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mantegazza.cdb.exceptions.InconsistentDatesException;
import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;

public class ComputerService {
	
	private ComputerDAO dao;
	private CompanyService companyService;
	private Logger logger = LoggerFactory.getLogger(ComputerService.class);

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	public void setComputerDAO(ComputerDAO computerDAO) {
		this.dao = computerDAO;
	}
	
	public Optional<Computer> getComputer(String name) {
		return dao.getByName(name);
	}
	
	public Optional<Computer> getComputer(long id) {
		return dao.getByID(id);
	}

	public ArrayList<Computer> getAllComputers() {
		return dao.getAll();
	}
	
	public ArrayList<Computer> getComputerSelection(int numberToGet, int lineOffset) {
		return dao.getSelection(numberToGet, lineOffset);
	}
	
	public int getCount() {
		return dao.getCount();
	}
	
	public void create(String computerName, Optional<LocalDate> introduced, Optional<LocalDate> discontinued, Optional<String> companyName) throws InconsistentDatesException {
		if (!areDatesConsistent(introduced, discontinued)) {
			logger.error("Dates in computer {} are inconsistent. Introduced: {}, discontinued: {}", computerName, introduced, discontinued);
			throw new InconsistentDatesException();
		}
		
		Computer computerToCreate = new Computer(computerName);
		if (introduced.isPresent()) {
			computerToCreate.setIntroductionDate(introduced.get());
		}
		if (discontinued.isPresent()) {
			computerToCreate.setDiscontinuationDate(discontinued.get());
		}
		if (companyName.isPresent()) {
			Optional<Company> companyToAdd = companyService.getCompany(companyName.get());
			if (companyToAdd.isPresent()) {
				computerToCreate.setCompany(companyToAdd.get());
			}
		}
		
		dao.create(computerToCreate);
	}
	
	/**
	 * Build a Computer object to give to the DAO to update
	 * @param oldName
	 * @param newComputerName If null, the new Computer object with have the previous name instead
	 * @param introduced
	 * @param discontinued
	 * @param companyName
	 * @throws InconsistentDatesException 
	 */
	public void update(String oldName, Optional<String> newComputerName, Optional<LocalDate> introduced, Optional<LocalDate> discontinued, Optional<String> companyName) throws InconsistentDatesException {
		if (!areDatesConsistent(introduced, discontinued)) {
			String computerName = (newComputerName.isEmpty()) ? oldName : newComputerName.get();
			logger.error("Dates in computer {} are inconsistent. Introduced: {}, discontinued: {}", computerName, introduced, discontinued);
			throw new InconsistentDatesException();
		}
		
		Computer newComputer = new Computer();
		if (newComputerName.isPresent()) {
			newComputer.setName(newComputerName.get());
		} else {
			newComputer.setName(oldName);
		}
		if (introduced.isPresent()) {
			newComputer.setIntroductionDate(introduced.get());
		}
		if (discontinued.isPresent()) {
			newComputer.setDiscontinuationDate(discontinued.get());
		}
		if (companyName.isPresent()) {
			Optional<Company> companyToAdd = companyService.getCompany(companyName.get());
			if (companyToAdd.isPresent()) {
				newComputer.setCompany(companyToAdd.get());
			}
		}
		
		dao.update(oldName, newComputer);
	}
	
	public void delete(String name) {
		dao.delete(name);
	}
	
	public void delete(long id) {
		dao.delete(id);
	}
	
	/**
	 * Returns true if the discontinued dates is after the introduced date, or if at least one of the dates is null.
	 * Return false if the discontinued dates is after the introduced date.
	 * @param introduced
	 * @param discontinued
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
