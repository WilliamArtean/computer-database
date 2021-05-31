package com.excilys.mantegazza.cdb.service;

import java.util.ArrayList;
import java.util.Optional;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.persistence.CompanyDAO;

public class CompanyService {
	
	private CompanyDAO companyDAO = new CompanyDAO();
	
	/**
	 * Set a CompanyDAO from which to get the companies from the database.
	 * @param companyDAO The CompanyDAO to use with this CompanyService
	 */
	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	/**
	 * Get a company from the CompanyDAO.
	 * @param name The name of the company to fetch
	 * @return An Optional containing a Company object matching the name, or an empty Optional if the company could not be found
	 */
	public Optional<Company> getCompany(String name) {
		return companyDAO.getByName(name);
	}
	
	/**
	 * Get a company from the CompanyDAO.
	 * @param id The id of the company to fetch
	 * @return An Optional containing a Company object matching the id, or an empty Optional if the company could not be found
	 */
	public Optional<Company> getCompany(long id) {
		return companyDAO.getByID(id);
	}
	
	/**
	 * Get all the companies from the CompanyDAO.
	 * @return An ArrayList of all the Company objects in the database
	 */
	public ArrayList<Company> getAllCompanies() {
		return companyDAO.getAll();
	}
	
	/**
	 * Get a selected number of companies from the database.
	 * @param numberToGet The number of companies to return.
	 * @param rowOffset The offset from which to start the selection. 0 to start from the first row (included)
	 * @return An ArrayList of Company objects in the database, of size numberToReturn at most (less if the end of the database has been reached)
	 */
	public ArrayList<Company> getCompanySelection(int numberToGet, int rowOffset) {
		return companyDAO.getSelection(numberToGet, rowOffset);
	}
	
}
