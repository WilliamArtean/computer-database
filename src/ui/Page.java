package ui;

import java.util.ArrayList;

import exceptions.InvalidPageNumberException;
import model.Company;
import model.Computer;

public class Page {

	private ArrayList<String> cdbObjects = new ArrayList<String>();;
	int totalPages;
	int currentPage;
	
	
	public void fillComputerList(ArrayList<Computer> computers) {
		this.cdbObjects.clear();
		for (Computer computer : computers) {
			this.cdbObjects.add(computer.getName());
		}
		currentPage = 0;
		totalPages = ((cdbObjects.size()-1) / 10)+1;
	}
	public void fillCompanyList(ArrayList<Company> companies) {
		this.cdbObjects.clear();
		for (Company company : companies) {
			this.cdbObjects.add(company.getName());
		}
		currentPage = 0;
		totalPages = ((cdbObjects.size()-1) / 10)+1;
	}
	
	/**
	 * Returns a string containing the names of the objects in the page,
	 * current page number and total page number,
	 * with a maximum of 10, or the number remaining in the last page.
	 * @param pageNumber number of the page to be displayed, starting from 1
	 * @throws InvalidPageNumberException 
	 */
	public String getObjectsInPage(int pageNumber) throws InvalidPageNumberException {
		this.currentPage = pageNumber-1;
		StringBuilder objectsToShow = new StringBuilder();
		
		if (pageNumber <= 0 || pageNumber > totalPages)
			throw new InvalidPageNumberException();
		
		for (int i = 10*currentPage; i<10*(currentPage + 1) && i < cdbObjects.size(); i++) {
			objectsToShow.append(cdbObjects.get(i)).append('\n');
		}
		objectsToShow.append(pageNumber).append('/').append(totalPages);
		
		return objectsToShow.toString();
	}

}
