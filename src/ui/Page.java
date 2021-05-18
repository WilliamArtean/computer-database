package ui;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Page {
	
	private int numberOfPages;
	private int currentPage;
	private ArrayList<String> list;
	
	public int getNumberOfPages() {
		return numberOfPages;
	}
	public int getCurrentPage() {
		return currentPage;
	}

	public void displayPage() {
		String listToDisplay = list.stream().collect(Collectors.joining("\n"));
		System.out.println(listToDisplay);
		
		StringBuilder pagination = new StringBuilder("\n");
		pagination.append(currentPage).append("/").append(numberOfPages);
		if (currentPage < numberOfPages) {
			pagination.append("\n").append("Press 'n' to move to the next page.");
		}
		if (currentPage > 1) {
			pagination.append("\n").append("Press 'p' to move to the previous page.");
		}
		pagination.append("\n").append("Press 'q' to quit page navigation.");
	}
	
}
