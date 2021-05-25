package com.excilys.mantegazza.cdb.model;

import java.time.LocalDate;

public class Computer {
	
	private String name;
	private long id;
	private LocalDate introductionDate;
	private LocalDate discontinuationDate;
	private Company company;
	
	private Computer(ComputerBuilder builder) {
		this.name = builder.name;
		this.id = builder.id;
		this.introductionDate = builder.introduced;
		this.discontinuationDate = builder.discontinued;
		this.company = builder.company;
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


	public static class ComputerBuilder {
		
		private String name;
		private long id;
		private LocalDate introduced;
		private LocalDate discontinued;
		private Company company;
		
		public ComputerBuilder() { }
		
		public ComputerBuilder(String name) {
			this.name = name;
		}
		
		public ComputerBuilder withName(String name) {
			this.name = name;
			return this;
		}
		
		public ComputerBuilder withID(long id) {
			this.id = id;
			return this;
		}
		
		public ComputerBuilder withIntroduced(LocalDate introduced) {
			this.introduced = introduced;
			return this;
		}
		
		public ComputerBuilder withDiscontinued(LocalDate discontinued) {
			this.discontinued = discontinued;
			return this;
		}
		
		public ComputerBuilder withCompany(Company company) {
			this.company = company;
			return this;
		}
		
		public Computer build() {
			return new Computer(this);
		}
		
	}
	
}
