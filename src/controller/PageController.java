package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.Computer;
import service.ComputerService;
import ui.Page;

public class PageController {

	private Page view;
	private ComputerService service;
	private int currentPageIndex = 0;
	private final int numberOfPages;
	private final int itemsPerPage = 10;
	private ArrayList<Computer> list = new ArrayList<Computer>();
	private ArrayList<String> nameList = new ArrayList<String>();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
	public int getNumberOfPages() {
		return numberOfPages;
	}
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	
	public PageController(Page view, ComputerService service) {
		this.view = view;
		this.service = service;
		this.numberOfPages = ((service.getCount() - 1) / itemsPerPage) + 1;
	}
	
	private String getInput() throws IOException {
		return this.br.readLine().substring(0, 1);
	}

	private void clear() {
		list.clear();
		nameList.clear();
	}
	
	private void newPage() {
		clear();
		list = service.getComputerSelection(itemsPerPage, 0);
		for (Computer computer : list) {
			nameList.add(computer.getName());
		}
		view.displayPage(nameList);
	}
	
	public void startNavigation() throws IOException {
		list.clear();
		nameList.clear();
		list = service.getComputerSelection(itemsPerPage, 0);
		for (Computer computer : list) {
			nameList.add(computer.getName());
		}
		view.displayPage(nameList);
		
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
	
	public void previousPage() {
		if (currentPageIndex > 0) {
			currentPageIndex--;
			clear();
			list = service.getComputerSelection(itemsPerPage, itemsPerPage * currentPageIndex);
			for (Computer computer : list) {
				nameList.add(computer.getName());
			}
			view.displayPage(nameList);
		}
	}
	
	public void nextPage() {
		if (currentPageIndex < (numberOfPages - 1)) {
			currentPageIndex++;
			clear();
			list = service.getComputerSelection(itemsPerPage, itemsPerPage * currentPageIndex);
			for (Computer computer : list) {
				nameList.add(computer.getName());
			}
			view.displayPage(nameList);
		}
	}
	
}
