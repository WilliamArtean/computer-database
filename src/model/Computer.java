package model;

import java.time.LocalDate;

public class Computer {
	
	private long id;
	private String name;
	private LocalDate introductionDate;
	private LocalDate discontinuationDate;
	private Company company;
	
	public Computer() {
		
	}
	
	public Computer(String name) {
		this.name = name;
	}

	public long getID() {
		return this.id;
	}
	public void setID(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
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
