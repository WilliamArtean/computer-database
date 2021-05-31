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
	
	public int getCount() {
		return count;
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
		
}
