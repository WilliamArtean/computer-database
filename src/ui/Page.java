package ui;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Page {
	
	public void displayPage(ArrayList<String> list) {
		String listToDisplay = list.stream().collect(Collectors.joining("\n"));
		System.out.println(listToDisplay);
		/*
		StringBuilder pagination = new StringBuilder("\n");
		pagination.append(currentPage).append("/").append(numberOfPages);
		if (currentPage < numberOfPages) {
			pagination.append("\n").append("Enter 'n' to move to the next page.");
		}
		if (currentPage > 1) {
			pagination.append("\n").append("Enter 'p' to move to the previous page.");
		}
		pagination.append("\n").append("Enter 'q' to quit page navigation.");
		*/
	}
	
}
