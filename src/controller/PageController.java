package controller;

import java.io.IOException;

import ui.Page;

public class PageController {

	private Page view;
	private int currentPage;
	private int numberOfPages;
	
	public int getNumberOfPages() {
		return numberOfPages;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	
	public PageController(Page view) {
		this.view = view;
	}
	
	public void startNavigation() throws IOException {
		
	}
	
}
