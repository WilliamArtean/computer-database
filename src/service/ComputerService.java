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
	
	public void update(String name, Computer computer) {
		dao.update(name, computer);
	}
	
	public void delete(String name) {
		dao.delete(name);
	}
	
	public void delete(long id) {
		dao.delete(id);
	}
}
