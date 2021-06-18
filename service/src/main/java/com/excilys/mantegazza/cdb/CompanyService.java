package com.excilys.mantegazza.cdb;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.excilys.mantegazza.cdb.dao.ICompanyDao;

@Service
public class CompanyService {
	
	private ICompanyDao companyDAO;
	
	public CompanyService(ICompanyDao companyDAO) {
		this.companyDAO = companyDAO;
	}

	/**
	 * Set a CompanyDAO from which to get the companies from the database.
	 * @param companyDAO The CompanyDAO to use with this CompanyService
	 */
	public void setCompanyDAO(ICompanyDao companyDAO) {
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
		return companyDAO.getById(id);
	}
	
	/**
	 * Get all the companies from the CompanyDAO.
	 * @return An ArrayList of all the Company objects in the database
	 */
	public ArrayList<Company> getAllCompanies() {
		return companyDAO.getAll();
	}
	
	public void delete(String name) {
		Optional<Company> companyToDelete = companyDAO.getByName(name);
		companyToDelete.ifPresent(c -> companyDAO.delete(c.getID()));
	}
	
}
