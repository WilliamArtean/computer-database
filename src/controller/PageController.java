package controller;

import java.io.IOException;
import java.util.ArrayList;

import model.Computer;
import service.ComputerService;
import ui.Page;

public class PageController {

	private Page view;
	private ComputerService service;
	private int currentPage = 0;
	private int numberOfPages;
	private final int itemsPerPage = 10;
	private ArrayList<Computer> list = new ArrayList<Computer>();
		
	public int getNumberOfPages() {
		return numberOfPages;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	
	public PageController(Page view, ComputerService service) {
		this.view = view;
		this.service = service;
		
	}
	
	public void startNavigation() throws IOException {
		
	}
	
}
