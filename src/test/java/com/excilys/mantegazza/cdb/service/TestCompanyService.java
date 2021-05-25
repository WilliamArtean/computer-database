package com.excilys.mantegazza.cdb.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.persistence.CompanyDAO;

@RunWith(MockitoJUnitRunner.class)
public class TestCompanyService {
	
	private CompanyService service;

	@Mock
	private CompanyDAO dao;
	private ArrayList<Company> companyDB;
	
	@Before
	public void setUpService() {
		companyDB = new ArrayList<Company>();
		companyDB.add(new Company.CompanyBuilder("Company 1").withID(1).build());
		companyDB.add(new Company.CompanyBuilder("Company 2").withID(2).build());
		companyDB.add(new Company.CompanyBuilder("Company 3").withID(3).build());
		companyDB.add(new Company.CompanyBuilder("Company 4").withID(4).build());
		
		
		service = new CompanyService();
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
		when(dao.getByID(companyDB.get(0).getID()))
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
	
	@Test
	public void testGetSelection() {
		ArrayList<Company> companySelection = new ArrayList<Company>();
		companySelection.add(companyDB.get(1));
		companySelection.add(companyDB.get(2));
		
		when(dao.getSelection(2, 1)).thenReturn(companySelection);
		
		ArrayList<Company> companiesReturned = service.getCompanySelection(2, 1);
		assertEquals(companiesReturned, companySelection);
	}
	
}