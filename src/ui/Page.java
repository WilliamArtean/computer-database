package ui;

import java.util.ArrayList;

import model.Company;
import model.Computer;

public class Page {

	private ArrayList<String> cdbObjects = new ArrayList<String>();;
	int totalPages = 0;
	int currentPage = -1;
	
	
	public void fillComputerList(ArrayList<Computer> computers) {
		this.clear();
		for (Computer computer : computers) {
			this.cdbObjects.add(computer.getName());
		}
		totalPages = ((cdbObjects.size()-1) / 10)+1;
	}
	public void fillCompanyList(ArrayList<Company> companies) {
		this.clear();
		for (Company company : companies) {
			this.cdbObjects.add(company.getName());
		}
		totalPages = ((cdbObjects.size()-1) / 10)+1;
	}
	
	/**
	 * Clears the list of names stores in the Page class.
	 * Resets the page numbers.
	 */
	public void clear() {
		this.cdbObjects.clear();
		currentPage = -1;
		totalPages = 0;
	}
	
	/**
	 * Returns a string containing the names of the objects in the page,
	 * current page number and total page number,
	 * with a maximum of 10, or the number remaining in the last page.
	 * 
	 * @param pageNumber number of the page to be displayed, starting from 1
	 * @return a string containing the object names, or null if the page number is incorrect
	 */
	public String showPage(int pageNumber) {
		this.currentPage = pageNumber-1;
		StringBuilder objectsToShow = new StringBuilder();
		
		if (pageNumber <= 0 || pageNumber > totalPages)
			return null;
		
		for (int i = 10*currentPage; i<10*(currentPage + 1) && i < cdbObjects.size(); i++) {
			objectsToShow.append(cdbObjects.get(i)).append('\n');
		}
		objectsToShow.append(pageNumber).append('/').append(totalPages);
		
		return objectsToShow.toString();
	}
	
	/**
	 * Moves the current page to the first page
	 */
	public void firstPage() {
		currentPage = 0;
	}
	
	public void lastPage() {
		currentPage = totalPages - 1;
	}
	
	/**
	 * Moves the current page to the next page.
	 * @return true if the page has been successfully incremented, and false if it is already at the last page
	 */
	public boolean nextPage() {
		if (currentPage < totalPages)
			currentPage++;
		return currentPage < totalPages;
	}
	public boolean previousPage() {
		if (currentPage >= 0) {
			currentPage--;
		}
		return currentPage >= 0;
	}
	
	public String showCurrentPage() {
		return showPage(currentPage + 1);
	}
	
	public boolean hasNextPage() {
		return currentPage < totalPages-1;
	}
	public boolean hasPreviousPage() {
		return currentPage < 0;
	}

}
