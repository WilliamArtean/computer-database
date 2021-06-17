package com.excilys.mantegazza.cdb.persistence;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.service.Page;

@Repository
public interface IComputerDao {

	Optional<Computer> getByID(long id);
	Optional<Computer> getByName(String name);
	
	ArrayList<Computer> getAll();
	ArrayList<Computer> getSelection(Page page);
	ArrayList<Computer> getSelection(int offset, int limit);
	
	int getCount();
	int getCount(Page page);
	
	void create(Computer computer);
	
	void update(long computerId, Computer updatedComputer);
	
	void delete(long id);
	void delete(ArrayList<Long> idsToDelete);
	
}
