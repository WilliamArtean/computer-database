package com.excilys.mantegazza.cdb.model;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((discontinuationDate == null) ? 0 : discontinuationDate.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((introductionDate == null) ? 0 : introductionDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;			
		}
		if (obj == null) {
			return false;			
		}
		if (getClass() != obj.getClass()) {
			return false;			
		}
		Computer other = (Computer) obj;
		if (company == null) {
			if (other.company != null) {
				return false;				
			}
		} else if (!company.equals(other.company)) {
			return false;			
		}
		if (discontinuationDate == null) {
			if (other.discontinuationDate != null) {
				return false;				
			}
		} else if (!discontinuationDate.equals(other.discontinuationDate)) {
			return false;			
		}
		if (id != other.id) {
			return false;			
		}
		if (introductionDate == null) {
			if (other.introductionDate != null) {
				return false;				
			}
		} else if (!introductionDate.equals(other.introductionDate)) {
			return false;
			
		}
		if (name == null) {
			if (other.name != null) {
				return false;				
			}
		} else if (!name.equals(other.name)) {
			return false;			
		}
		return true;
	}


	
	
}
