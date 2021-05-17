package service;

import java.util.ArrayList;
import java.util.Optional;

import model.Company;
import persistence.CompanyDAO;

public class CompanyService {
	
	private CompanyDAO companyDAO;
	
	public Optional<Company> getCompany(String name) {
		return companyDAO.getByName(name);
	}
	
	public Optional<Company> getCompany(long id) {
		return companyDAO.getByID(id);
	}
	
	public ArrayList<Company> getAllCompanies() {
		return companyDAO.getAll();
	}
	
	public ArrayList<Company> getCompanySelection(int numberToGet, int lineOffset) {
		return companyDAO.getSelection(numberToGet, lineOffset);
	}
	
}
