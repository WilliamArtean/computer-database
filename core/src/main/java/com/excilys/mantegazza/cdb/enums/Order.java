package com.excilys.mantegazza.cdb.enums;

public enum Order {
	ascending("ASC"), descending("DESC");
	
	private final String word;

	Order(String name) {
		this.word = name;
	}
	
	public String getSQLKeyword() {
		return this.word;
	}
	
}
