package com.excilys.mantegazza.cdb.dto;

public class ComputerDTO {

	private String name = "";
	private long id;
	private String introductionDate = "";
	private String discontinuationDate = "";
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

	public String getIntroductionDate() {
		return introductionDate;
	}

	public void setIntroductionDate(String introductionDate) {
		this.introductionDate = introductionDate;
	}

	public String getDiscontinuationDate() {
		return discontinuationDate;
	}

	public void setDiscontinuationDate(String discontinuationDate) {
		this.discontinuationDate = discontinuationDate;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	
}
