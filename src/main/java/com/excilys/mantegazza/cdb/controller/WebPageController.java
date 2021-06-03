package com.excilys.mantegazza.cdb.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mantegazza.cdb.dto.ComputerDTO;
import com.excilys.mantegazza.cdb.dto.mappers.ComputerDTOMapper;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.utils.SearchOrderColumn;

public class WebPageController {
	private int currentPageIndex = 0;
	private int itemsPerPage = 10;
	private int count;
	private int numberOfPages;
	private String searchTerm;
	private SearchOrderColumn columnToOrderBy = SearchOrderColumn.none;
	private boolean ascendantOrder = true;
	
	private ArrayList<Computer> list = new ArrayList<Computer>();
	private ArrayList<ComputerDTO> dtoList = new ArrayList<ComputerDTO>();

	private ComputerService service;
	private ComputerDTOMapper dtoMapper = new ComputerDTOMapper();
	private Logger logger = LoggerFactory.getLogger(WebPageController.class);
	
	public WebPageController() {
		this.service = new ComputerService();
		refreshPage();
	}
	
	
	public void refreshPage() {
		logger.debug("Refreshing page");
		clear();
		
		if (searchTerm == null) {
			refreshListPage();
		} else {
			refreshSearchPage();
		}
		
		dtoList = dtoMapper.computersToDTOArray(list);
	}
	
	private void refreshListPage() {
		this.count = service.getCount();
		this.numberOfPages = ((count - 1) / itemsPerPage) + 1;
		if (currentPageIndex >= numberOfPages) {
			currentPageIndex = numberOfPages - 1;
		}
		
		int rowOffset = itemsPerPage * currentPageIndex;
		logger.debug("No item to search - call to {} computers from database", itemsPerPage);
		
		if (SearchOrderColumn.none.equals(columnToOrderBy)) {
			fillComputerList(itemsPerPage, rowOffset);
		} else {
			fillComputerListOrdered(itemsPerPage, rowOffset, columnToOrderBy, ascendantOrder);
		}
	}
	
	private void fillComputerList(int size, int rowOffset) {
		list = service.getComputerSelection(size, rowOffset);
	}
	private void fillComputerListOrdered(int size, int rowOffset, SearchOrderColumn orderColumn, boolean ascendant) {
		list = service.getComputerSelection(size, rowOffset, orderColumn, ascendant);
	}
	
	private void refreshSearchPage() {
		this.count = service.getSearchCount(searchTerm);
		this.numberOfPages = ((count - 1) / itemsPerPage) + 1;
		if (currentPageIndex >= numberOfPages) {
			currentPageIndex = 0;
		}
		
		int rowOffset = itemsPerPage * currentPageIndex;
		logger.debug("Searching for computers with name matching '{}' - call to {} computers from database", searchTerm, itemsPerPage);
		
		if (SearchOrderColumn.none.equals(columnToOrderBy)) {
			fillComputerSearch(searchTerm, itemsPerPage, rowOffset);
		} else {
			fillComputerSearchOrdered(searchTerm, itemsPerPage, rowOffset, columnToOrderBy, ascendantOrder);
		}
	}
	
	private void fillComputerSearch(String name, int size, int rowOffset) {
		list = service.search(name, size, rowOffset);
	}
	private void fillComputerSearchOrdered(String name, int size, int rowOffset, SearchOrderColumn orderColumn, boolean ascendant) {
		list = service.search(name, size, rowOffset, orderColumn, ascendant);
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
	 */
	public void setToPage(int pageNumber) {
		if (pageNumber < 1) {
			currentPageIndex = 0;
		} else if (pageNumber > numberOfPages) {
			currentPageIndex = numberOfPages - 1;
		} else {
			this.currentPageIndex = pageNumber - 1;
		}
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

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		logger.debug("Setting search term '{}'", searchTerm);
		this.searchTerm = searchTerm;
	}
	
	public void cancelSearch() {
		logger.debug("Removing search term '{}'", searchTerm);
		this.searchTerm = null;
	}
	
	public void setOrder(String order) {
		SearchOrderColumn newOrderColumn;
		switch (order) {
		case "computerName":
			newOrderColumn = SearchOrderColumn.computerName;
			break;
		case "introduced":
			newOrderColumn = SearchOrderColumn.introduced;
			break;
		case "discontinued":
			newOrderColumn = SearchOrderColumn.discontinued;
			break;
		case "companyName":
			newOrderColumn = SearchOrderColumn.companyName;
			break;
		default:
			newOrderColumn = SearchOrderColumn.none;
			break;
		}
		
		if (columnToOrderBy.equals(newOrderColumn) && !SearchOrderColumn.none.equals(newOrderColumn)) {
			ascendantOrder = !ascendantOrder;
		} else {
			ascendantOrder = true;
		}
		this.columnToOrderBy = newOrderColumn;
	}
	
	public void resetOrder() {
		setOrder("none");
	}
}
