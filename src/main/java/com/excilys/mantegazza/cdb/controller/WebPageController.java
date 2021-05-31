package com.excilys.mantegazza.cdb.controller;

import java.util.ArrayList;

import com.excilys.mantegazza.cdb.dto.ComputerDTO;
import com.excilys.mantegazza.cdb.dto.mappers.ComputerDTOMapper;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.service.ComputerService;

public class WebPageController {
	private ComputerService service;
	private int currentPageIndex = 0;
	private int numberOfPages;
	private int itemsPerPage = 10;
	private int count;
	private ArrayList<Computer> list = new ArrayList<Computer>();
	private ArrayList<ComputerDTO> dtoList = new ArrayList<ComputerDTO>();
	private ComputerDTOMapper dtoMapper = new ComputerDTOMapper();
	
	public WebPageController() {
		this.service = new ComputerService();
		this.count = service.getCount();
		this.numberOfPages = ((count - 1) / itemsPerPage) + 1;
	}
	
	
	private void refreshPage() {
		clear();
		int rowOffset = itemsPerPage * currentPageIndex;
		list = service.getComputerSelection(itemsPerPage, rowOffset);
		dtoList = dtoMapper.computersToDTOArray(list);
	}
	
	private void clear() {
		list.clear();
		dtoList.clear();
	}
	

	public ArrayList<ComputerDTO> getFirstPage() {
		this.currentPageIndex = 0;
		refreshPage();
		return dtoList;
	}

	public ArrayList<ComputerDTO> previousPage() {
		if (currentPageIndex > 0) {
			currentPageIndex--;
			refreshPage();
		}
		return dtoList;
	}
	
	public ArrayList<ComputerDTO> nextPage() {
		if (currentPageIndex < (numberOfPages - 1)) {
			currentPageIndex++;
			refreshPage();
		}
		return dtoList;
	}
	
	public ArrayList<ComputerDTO> setToPage(int pageIndex) {
		if (pageIndex >= 0 && pageIndex < numberOfPages) {
			this.currentPageIndex = pageIndex;
			refreshPage();
		}
		return dtoList;
	}
		
	
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public int getCount() {
		return count;
	}
	
	public int getItemsPerPage() {
		return itemsPerPage;
	}
	
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
	
	public int getNumberOfPages() {
		return numberOfPages;
	}
	
}
