package com.excilys.mantegazza.cdb.dto;

public class ComputerDTO {

	private String name = "";
	private long id;
	private String introductionDate = "";
	private String discontinuationDate = "";
	private String companyName = "";
	
	public ComputerDTO() {
		
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

	public String getIntroduced() {
		return introductionDate;
	}

	public void setIntroduced(String introductionDate) {
		this.introductionDate = introductionDate;
	}

	public String getDiscontinued() {
		return discontinuationDate;
	}

	public void setDiscontinued(String discontinuationDate) {
		this.discontinuationDate = discontinuationDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyId) {
		this.companyName = companyId;
	}
	
}
