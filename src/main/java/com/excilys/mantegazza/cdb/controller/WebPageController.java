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
	
	/**
	 * Set the page controller to load the specified page number.
	 * @param pageNumber Number of the page to load, the first page being 1.
	 * @return An array of computer dto in the page
	 */
	public ArrayList<ComputerDTO> setToPage(int pageNumber) {
		if (pageNumber >= 0 && pageNumber <= numberOfPages) {
			this.currentPageIndex = pageNumber - 1;
			refreshPage();
		}
		return dtoList;
	}
	
	public ArrayList<ComputerDTO> getCurrentPage() {
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
		this.currentPageIndex = 0;
		this.itemsPerPage = itemsPerPage;
		this.numberOfPages = ((count - 1) / itemsPerPage) + 1;
	}
	
	public int getNumberOfPages() {
		return numberOfPages;
	}
	
}
