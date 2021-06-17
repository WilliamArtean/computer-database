package com.excilys.mantegazza.cdb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.persistence.CompanyDAO;

@ExtendWith(MockitoExtension.class)
public class TestCompanyService {
	
	private CompanyService service;

	@Mock
	private CompanyDAO dao;
	private ArrayList<Company> companyDB;
	
	@BeforeEach
	public void setUpService() {
		companyDB = new ArrayList<Company>();
		companyDB.add(new Company.CompanyBuilder("Company 1").withID(1).build());
		companyDB.add(new Company.CompanyBuilder("Company 2").withID(2).build());
		companyDB.add(new Company.CompanyBuilder("Company 3").withID(3).build());
		companyDB.add(new Company.CompanyBuilder("Company 4").withID(4).build());
		
		
		service = new CompanyService(dao);
		service.setCompanyDAO(dao);
	}
	
	@Test
	public void testGetByName() {
		when(dao.getByName(companyDB.get(0).getName()))
		.thenReturn(Optional.of(companyDB.get(0)));
		
		Optional<Company> company1 = Optional.of(companyDB.get(0));
		Optional<Company> returnedCompanyByName = service.getCompany(company1.get().getName());
		assertEquals(company1, returnedCompanyByName);
	}
	
	@Test
	public void testGetByID() {
		when(dao.getById(companyDB.get(0).getID()))
		.thenReturn(Optional.of(companyDB.get(0)));

		Optional<Company> company1 = Optional.of(companyDB.get(0));
		Optional<Company> returnedCompanyByID = service.getCompany(company1.get().getID());
		assertEquals(company1, returnedCompanyByID);
	}
	
	@Test
	public void testGetAll() {
		when(dao.getAll()).thenReturn(companyDB);
		ArrayList<Company> companiesReturned = service.getAllCompanies();
		assertEquals(companiesReturned, companyDB);
	}
	
}
