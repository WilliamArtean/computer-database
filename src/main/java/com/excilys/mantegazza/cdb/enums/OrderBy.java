package com.excilys.mantegazza.cdb.enums;

public enum OrderBy {
	none("computer.id"), computerName("computer.name"), introduced("introduced"), discontinued("discontinued"), companyName("company.name");

	private final String word;
	
	OrderBy(String name) {
		this.word = name;
	}
	
	public static OrderBy getOrderBy(String name) {
		switch (name) {
		case "computerName":
			return OrderBy.computerName;
		case "introduced":
			return OrderBy.introduced;
		case "discontinued":
			return OrderBy.discontinued;
		case "companyName":
			return OrderBy.companyName;
		default:
			return OrderBy.none;
		}
	}
	
	public String getSQLKeyword() {
		return this.word;
	}
}
