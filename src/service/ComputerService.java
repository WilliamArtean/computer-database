package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import model.Company;
import model.Computer;
import persistence.ComputerDAO;

public class ComputerService {
	
	private ComputerDAO dao;
	private CompanyService companyService;

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
	
	public void create(String computerName, Optional<LocalDate> introduced, Optional<LocalDate> discontinued, Optional<String> companyName) {
		/*if (introduced.isPresent() && discontinued.isPresent()) {
			if (introduced.get().isAfter(discontinued.get())) {
				throw new InconsistentDatesException();
			}
		}*/
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
	 */
	public void update(String oldName, Optional<String> newComputerName, Optional<LocalDate> introduced, Optional<LocalDate> discontinued, Optional<String> companyName) {
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
}
