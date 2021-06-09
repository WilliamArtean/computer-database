package com.excilys.mantegazza.cdb.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.persistence.ComputerDAO;

@Service
public class ComputerService {
	
	@Autowired
	private ComputerDAO dao;
	
	public void setComputerDAO(ComputerDAO dao) {
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
		//TODO Validation?
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
		dao.delete(name);
	}
	
}
