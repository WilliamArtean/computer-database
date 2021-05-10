package model;

import java.time.LocalDate;

public class Computer {
	
	private String name;
	private LocalDate introductionDate;
	private LocalDate discontinuationDate;
	private Company manufacturer;
	
	
	public Computer(String name) {
		this.name = name;
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

	public Company getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Company manufacturer) {
		this.manufacturer = manufacturer;
	}
	
}
