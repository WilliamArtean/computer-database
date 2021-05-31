package com.excilys.mantegazza.cdb.dto.mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import com.excilys.mantegazza.cdb.dto.ComputerDTO;
import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.model.Computer.ComputerBuilder;
import com.excilys.mantegazza.cdb.service.CompanyService;

public class ComputerDTOMapper {
	
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private CompanyService companyService = new CompanyService();

	public ComputerDTO computerToDTO(Computer computer) {
		ComputerDTO computerDTO = new ComputerDTO();
		if (computer.getName() != null) {
			computerDTO.setName(computer.getName());
		}
		
		computerDTO.setId(computer.getID());
		
		if (computer.getIntroductionDate() != null) {
			computerDTO.setIntroductionDate(computer.getIntroductionDate().toString());
		}
		if (computer.getDiscontinuationDate() != null) {
			computerDTO.setDiscontinuationDate(computer.getDiscontinuationDate().toString());
		}
		if (computer.getCompany() != null) {
			computerDTO.setCompanyId(computer.getCompany().getID());
		}
		
		return computerDTO;
	}
	
	public ArrayList<ComputerDTO> computersToDTOArray(ArrayList<Computer> computers) {
		ArrayList<ComputerDTO> computerDTOArray = new ArrayList<ComputerDTO>();
		for (Computer computer : computers) {
			computerDTOArray.add(computerToDTO(computer));
		}
		return computerDTOArray;
	}
	
	public Computer dtoToComputer(ComputerDTO computerDTO) {
		ComputerBuilder computerbuilder = new Computer.ComputerBuilder();
		if (!computerDTO.getName().isEmpty()) {
			computerbuilder = computerbuilder.withName(computerDTO.getName());
		}
		computerbuilder = computerbuilder.withID(computerDTO.getId());
		
		if (!computerDTO.getIntroductionDate().isEmpty()) {
			computerbuilder = computerbuilder.withIntroduced(LocalDate.parse(computerDTO.getIntroductionDate(), df));
		}
		if (!computerDTO.getDiscontinuationDate().isEmpty()) {
			computerbuilder = computerbuilder.withDiscontinued(LocalDate.parse(computerDTO.getDiscontinuationDate(), df));
		}
		
		Optional<Company> company = companyService.getCompany(computerDTO.getCompanyId());
		if (company.isPresent()) {
			computerbuilder = computerbuilder.withCompany(company.get());
		}
		
		return computerbuilder.build();
	}

}
