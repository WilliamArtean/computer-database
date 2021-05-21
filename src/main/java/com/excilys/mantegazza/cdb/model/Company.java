package com.excilys.mantegazza.cdb.model;

public class Company {
	
	//Required parameters
	private String name;
	//Optional parameters
	private long id;
	
	private Company(CompanyBuilder builder) {
		this.name = builder.name;
		this.id = builder.id;
	}
	
	public long getID() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		sb.append(this.id);
		sb.append(", ");
		sb.append(this.name);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Company other = (Company) obj;
		if (id != other.id) {
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
	
	public static class CompanyBuilder {
		
		//Required parameters
		private String name;
		//Optional parameters
		private long id;
		
		public CompanyBuilder(String name) {
			this.name = name;
		}
		
		public CompanyBuilder withID(long id) {
			this.id = id;
			return this;
		}
		
		public Company build() {
			return new Company(this);
		}
		
	}
	
}
