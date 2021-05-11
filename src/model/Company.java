package model;

public class Company {
	
	private long id;
	private String name;
	
	public Company(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Company(String name) {
		this.name = name;
	}
	
	public long getID() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	
}
