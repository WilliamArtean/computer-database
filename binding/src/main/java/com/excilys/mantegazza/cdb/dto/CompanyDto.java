package com.excilys.mantegazza.cdb.dto;

public class CompanyDto {

	private String name = "";
	private long id;
	
	public CompanyDto() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
