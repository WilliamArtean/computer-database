package model;

import java.util.Date;

public class Computer {
	
	private String name;
	private Date introductionDate;
	private Date discontinuationDate;
	private Company company;
	
	
	public Computer(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getIntroductionDate() {
		return introductionDate;
	}

	public void setIntroductionDate(Date introductionDate) {
		this.introductionDate = introductionDate;
	}

	public Date getDiscontinuationDate() {
		return discontinuationDate;
	}

	public void setDiscontinuationDate(Date discontinuationDate) {
		this.discontinuationDate = discontinuationDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder('[');
		sb.append(this.name);
		sb.append(", ").append(this.introductionDate);
		sb.append(", ").append(this.discontinuationDate);
		sb.append(", ");
		if (this.company != null) {
			sb.append(company.getName());
		} else {
			sb.append("null");
		}
		sb.append(']');
		return sb.toString();
	}
	
	
	
}
