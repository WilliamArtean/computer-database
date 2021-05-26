package com.excilys.mantegazza.cdb.dto;

import java.time.LocalDate;

public class ComputerDTO {

	private String name;
	private long id;
	private LocalDate introductionDate;
	private LocalDate discontinuationDate;
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

	public LocalDate getIntroductionDate() {
		return introductionDate;
	}

	public void setIntroductionDate(LocalDate introductionDate) {
		this.introductionDate = introductionDate;
	}

	public LocalDate getDiscontinuationDate() {
		return discontinuationDate;
	}

	public void setDiscontinuationDate(LocalDate discontinuationDate) {
		this.discontinuationDate = discontinuationDate;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	
}
