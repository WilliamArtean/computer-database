package com.excilys.mantegazza.cdb.enums;

public enum OrderBy {
	none("computer.id"), computerName("computer.name"), introduced("introduced"), discontinued("discontinued"), companyName("company.name");

	private final String name;
	
	OrderBy(String name) {
		this.name = name;
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
	
	public String getName() {
		return this.name;
	}
}
