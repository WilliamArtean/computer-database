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
	private int currentPage = 0;
	private final int numberOfPages;
	private final int itemsPerPage = 10;
	private ArrayList<Computer> list = new ArrayList<Computer>();
	private ArrayList<String> nameList = new ArrayList<String>();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
	public int getNumberOfPages() {
		return numberOfPages;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	
	private String getInput() throws IOException {
		return this.br.readLine().substring(0, 1);
	}
	
	public PageController(Page view, ComputerService service) {
		this.view = view;
		this.service = service;
		this.numberOfPages = service.getCount();
	}
	
	public void startNavigation() throws IOException {
		
	}
	
}
