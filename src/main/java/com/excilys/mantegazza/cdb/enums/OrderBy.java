package com.excilys.mantegazza.cdb.enums;

public enum OrderBy {
	none("none"), computerName("computerName"), introduced("introduced"), discontinued("discontinued"), companyName("companyName");

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
}
