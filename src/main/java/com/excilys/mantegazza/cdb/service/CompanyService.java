package com.excilys.mantegazza.cdb.service;

import java.util.ArrayList;
import java.util.Optional;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.persistence.CompanyDAO;

public class CompanyService {
	
	private CompanyDAO companyDAO;
	
	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

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
