package com.excilys.mantegazza.cdb.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "computer")
public class ComputerPersistenceDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "introduced")
	private String introduced;
	@Column(name = "discontinued")
	private String discontinued;
	@ManyToOne
	//@Column(name = "company_id")
	@JoinColumn(name = "company_id")
	private CompanyPersistenceDto companyDto;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduced() {
		return introduced;
	}
	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}
	public String getDiscontinued() {
		return discontinued;
	}
	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}
	public CompanyPersistenceDto getCompanyDto() {
		return companyDto;
	}
	public void setCompanyDto(CompanyPersistenceDto companyDto) {
		this.companyDto = companyDto;
	}
	
}
