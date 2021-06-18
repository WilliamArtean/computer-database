package com.excilys.mantegazza.cdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.excilys.mantegazza.cdb.enums.Order;
import com.excilys.mantegazza.cdb.enums.OrderBy;


@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Page {

	private int currentPageIndex = 0;
	private int itemsPerPage = 10;
	private int count;
	private int numberOfPages;
	private String searchTerm = "";	
	private OrderBy orderBy = OrderBy.none;
	private Order order = Order.ascending;
	
	private Logger logger = LoggerFactory.getLogger(Page.class);
	
	
	public void setCount(int count) {
		this.count = count;
		this.numberOfPages = ((count - 1) / itemsPerPage) + 1;
		logger.debug("Set numberOfPages to {}", numberOfPages);
		if (currentPageIndex >= numberOfPages) {
			currentPageIndex = numberOfPages - 1;
		}
	}
	
	
	public void setToPage(int pageNumber) {
		if (pageNumber < 1) {
			currentPageIndex = 0;
		} else if (pageNumber > numberOfPages) {
			currentPageIndex = numberOfPages - 1;
		} else {
			this.currentPageIndex = pageNumber - 1;
		}
	}
	
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
		this.numberOfPages = ((count - 1) / itemsPerPage) + 1;
		this.currentPageIndex = 0;
	}
	
	public int getItemsPerPage() {
		return itemsPerPage;
	}
	
	public int getNumberOfPages() {
		return numberOfPages;
	}

	public int getRowOffset() {
		return itemsPerPage * currentPageIndex;
	}
	
	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		logger.debug("Setting new search term '{}'", searchTerm);
		this.searchTerm = searchTerm;
		resetOrderBy();
	}
	
	public void cancelSearch() {
		logger.debug("Removing search term '{}'", searchTerm);
		this.searchTerm = "";
	}

	
	public void setOrderBy(String orderBy) {
		OrderBy newOrderBy = OrderBy.getOrderBy(orderBy);
		
		if (!OrderBy.none.equals(newOrderBy) && this.orderBy.equals(newOrderBy)) {
			flipOrder();
		} else {
			this.order = Order.ascending;
		}
		this.orderBy = newOrderBy;
		
		logger.debug("OrderBy set to {}, order: {}", this.orderBy.name(), this.order.name());
	}
	
	public OrderBy getOrderBy() {
		return orderBy;
	}

	public Order getOrder() {
		return order;
	}


	private void flipOrder() {
		if (this.order.equals(Order.ascending)) {
			this.order = Order.descending;
		} else {
			this.order = Order.ascending;
		}
	}
	
	public void resetOrderBy() {
		logger.debug("Reseting OrderBy");
		setOrderBy("none");
	}
}
