package com.excilys.mantegazza.cdb.ui;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class CLIPageView {
	
	/**
	 * Displays a list of names in the CLI.
	 * @param list An ArrayList of String
	 */
	public void displayPage(ArrayList<String> list) {
		String listToDisplay = list.stream().collect(Collectors.joining("\n"));
		System.out.println(listToDisplay);
	}
	
	/**
	 * Displays the current page number, total page and commands for the user to navigate bewteen pages.
	 * @param pageIndex The current page index. The first page is index 0, the second page is index 1,...
	 * @param numberOfPages The total number of pages
	 */
	public void displayPagination(int pageIndex, int numberOfPages) {
		StringBuilder pagination = new StringBuilder();
		pagination.append(pageIndex + 1).append("/").append(numberOfPages);
		if (pageIndex > 0) {
			pagination.append("\n").append("Enter 'p' to move to the previous page.");
		}
		if ((pageIndex + 1) < numberOfPages) {
			pagination.append("\n").append("Enter 'n' to move to the next page.");
		}
		pagination.append("\n").append("Enter 'q' to quit page navigation.");
		
		System.out.println(pagination);
	}
	
}
