package com.excilys.mantegazza.cdb.dto;

public class ComputerDTO {

	private String name = "";
	private long id;
	private String introduced = "";
	private String discontinued = "";
	private String companyName = "";
	private long companyId;
	
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
		return introduced;
	}

	public void setIntroduced(String introductionDate) {
		this.introduced = introductionDate;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(String discontinuationDate) {
		this.discontinued = discontinuationDate;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	
}
