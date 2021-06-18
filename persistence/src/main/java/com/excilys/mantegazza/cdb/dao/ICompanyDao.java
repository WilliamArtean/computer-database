package com.excilys.mantegazza.cdb.dao;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.excilys.mantegazza.cdb.Company;

@Repository
public interface ICompanyDao {

	Optional<Company> getById(long id);
	Optional<Company> getByName(String name);
	
	int getCount();
	ArrayList<Company> getAll();
	
	void delete(long id);
}
