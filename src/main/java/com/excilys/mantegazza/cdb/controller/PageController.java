package com.excilys.mantegazza.cdb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.ui.Page;

public class PageController {

	private Page view;
	private ComputerService service;
	private int currentPageIndex = 0;
	private final int numberOfPages;
	private final int itemsPerPage = 10;
	private ArrayList<Computer> list = new ArrayList<Computer>();
	private ArrayList<String> nameList = new ArrayList<String>();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private Logger logger = LoggerFactory.getLogger(PageController.class);
	
	/**
	 * @return the total number of pages in the view.
	 */
	public int getNumberOfPages() {
		return numberOfPages;
	}
	/**
	 * @return the index of the currently displayed page. First page index is 0, second page index is 1,...
	 */
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	
	/**
	 * Creates a PageController to show a list of computers using a view with pagination.
	 * @param view The Page view the PageController will send the data to output.
	 * @param service The service for Computer objects the PageController will use
	 */
	public PageController(Page view, ComputerService service) {
		this.view = view;
		this.service = service;
		this.numberOfPages = ((service.getCount() - 1) / itemsPerPage) + 1;
	}
	
	/**
	 * Returns the user input from the CLI.
	 * @return a String containing what the user wrote in the CLI
	 * @throws IOException
	 */
	private String getInput() throws IOException {
		return this.br.readLine().substring(0, 1);
	}

	/**
	 * Clear both the list of stored Computer objects and the list of matching computer names.
	 */
	private void clear() {
		list.clear();
		nameList.clear();
	}
	
	/**
	 * Fetch the computers matching the current page index from the database,
	 * and asks the Page view to show them.
	 */
	private void newPage() {
		clear();
		logger.debug("Selecting {} computers from ComputerService", itemsPerPage);
		list = service.getComputerSelection(itemsPerPage, itemsPerPage * currentPageIndex);
		for (Computer computer : list) {
			nameList.add(computer.getName());
		}
		view.displayPage(nameList);
	}
	
	/**
	 * Displays the first page of the computers list, and asks for the user input to navigate between pages.
	 * Ends when the user input 'q' to exit.
	 * @throws IOException
	 */
	public void startNavigation() throws IOException {
		newPage();
		
		String userInput = "";
		do {
			view.displayPagination(currentPageIndex, numberOfPages);
			userInput = getInput();
			switch (userInput) {
			case "p":
				previousPage();
				break;
			case "n":
				nextPage();
				break;
			case "q":
				break;
			default:
				System.out.println("Wrong input");
				break;
			}
		} while (!"q".equals(userInput));
	}
	
	/**
	 * If the current page is after the first page, increments the page index and refreshes the page.
	 * Do nothing if the current page is already the first one.
	 */
	public void previousPage() {
		if (currentPageIndex > 0) {
			currentPageIndex--;
			newPage();
		}
	}
	
	/**
	 * If the current page is before the last page, decrements the page index and refreshes the page.
	 * Do nothing if the current page is already the last one.
	 */
	public void nextPage() {
		if (currentPageIndex < (numberOfPages - 1)) {
			currentPageIndex++;
			newPage();
		}
	}
	
}
