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
}
