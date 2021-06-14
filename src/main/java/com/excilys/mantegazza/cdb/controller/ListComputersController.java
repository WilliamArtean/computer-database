package com.excilys.mantegazza.cdb.controller;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.excilys.mantegazza.cdb.dto.ComputerDto;
import com.excilys.mantegazza.cdb.service.ComputerService;
import com.excilys.mantegazza.cdb.service.Page;

@Controller
public class ListComputersController {

	public static final String VIEW_COMPUTER = "dashboard";
	public static final String COMPUTER_LIST = "computerList";
	
	public static final String ATT_PAGE = "page";
	public static final String ATT_SEARCH_TERM = "searchTerm";
	
	public static final String PARAM_PAGE_NUMBER = "page";
	public static final String PARAM_ITEMS_PER_PAGE = "itemsPerPage";
	public static final String PARAM_ITEMS_TO_DELETE = "cb";
	public static final String PARAM_SEARCH = "search";
	public static final String PARAM_ORDER = "orderBy";

	private ComputerService computerService;
	private Page page;
	private Logger logger = LoggerFactory.getLogger(ListComputersController.class);
	
	
	public ListComputersController(ComputerService computerService, Page page) {
		this.computerService = computerService;
		this.page = page;
	}
	
	
	@GetMapping("/computers")
	public ModelAndView processParameters(
			@RequestParam(name = PARAM_SEARCH, required = false) String search,
			@RequestParam(name = PARAM_ORDER, required = false) String orderBy,
			@RequestParam(name = PARAM_ITEMS_PER_PAGE, required = false, defaultValue = "-1") int itemsPerPage,
			@RequestParam(name = PARAM_PAGE_NUMBER, required = false, defaultValue = "-1") int pageNumber
			) {
		ModelAndView modelAndView = new ModelAndView("dashboard");
		setSessionPage(modelAndView);
		
		if (search != null) {
			page.setSearchTerm(search);
		}
		if (orderBy != null) {
			page.setOrderBy(orderBy);
		}
		if (itemsPerPage != -1) {
			page.setItemsPerPage(itemsPerPage);
		}
		if (pageNumber != -1) {
			page.setToPage(pageNumber);
		}
		
		page.refreshPage();
		ArrayList<ComputerDto> computerArray = page.getCurrentPage();
		modelAndView.addObject(COMPUTER_LIST, computerArray);
		
		return modelAndView;
	}
	
	@PostMapping("/computers")
	public RedirectView delete(@RequestParam(name = PARAM_ITEMS_TO_DELETE, required = false) String[] checkboxesIds) {
		ArrayList<Long> idsToDelete = new ArrayList<Long>();
		Stream.of(checkboxesIds).forEach(checkboxId -> idsToDelete.add(Long.parseLong(checkboxId)));
		computerService.delete(idsToDelete);
		
		return new RedirectView("/computers", true);
	}
	
	private void setSessionPage(ModelAndView modelAndView) {
		if (modelAndView.getModel().get(ATT_PAGE) == null) {
			modelAndView.addObject(ATT_PAGE, this.page);
		}
	}
	
}
