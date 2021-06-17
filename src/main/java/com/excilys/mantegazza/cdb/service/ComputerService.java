package com.excilys.mantegazza.cdb.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;
import com.excilys.mantegazza.cdb.persistence.IComputerDao;

@Service
public class ComputerService {
	
	private IComputerDao dao;
	
	public ComputerService(IComputerDao dao) {
		this.dao = dao;
	}

	public void setComputerDAO(IComputerDao dao) {
		this.dao = dao;
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
	
	public ArrayList<Computer> getComputerSelection(Page page) {
		return dao.getSelection(page);
	}
	
	public int getCount() {
		return dao.getCount();
	}
	
	public int getCount(Page page) {
		return dao.getCount(page);
	}
	
	public void create(Computer computerToCreate) {
		dao.create(computerToCreate);
	}
	
	public void update(long computerId, Computer newComputer) {
		dao.update(computerId, newComputer);
	}
		
	public void delete(long id) {
		dao.delete(id);
	}
	
	public void delete(ArrayList<Long> idsToDelete) {
		dao.delete(idsToDelete);
	}
	
	public void delete(String name) {
		Optional<Computer> computerToDelete = dao.getByName(name);
		computerToDelete.ifPresent(c -> dao.delete(c.getID()));
	}

	public ArrayList<Computer> getComputerSelection(int offset, int limit) {
		return dao.getSelection(offset, limit);
	}
	
}
