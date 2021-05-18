package service;

import java.util.ArrayList;
import java.util.Optional;

import model.Computer;
import persistence.ComputerDAO;

public class ComputerService {
	
	private ComputerDAO dao;

	public void setComputerDAO(ComputerDAO computerDAO) {
		this.dao = computerDAO;
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
	
	public ArrayList<Computer> getComputerSelection(int numberToGet, int lineOffset) {
		return dao.getSelection(numberToGet, lineOffset);
	}
	
	public void create(Computer computer) {
		dao.create(computer);
	}
	
	public void update(String name, Computer computer) {
		dao.update(name, computer);
	}
	
	public void delete(String name) {
		dao.delete(name);
	}
	
	public void delete(long id) {
		dao.delete(id);
	}
}
