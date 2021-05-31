package com.excilys.mantegazza.cdb.dto.mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import com.excilys.mantegazza.cdb.dto.CompanyDTO;
import com.excilys.mantegazza.cdb.dto.ComputerDTO;
import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.model.Computer.ComputerBuilder;
import com.excilys.mantegazza.cdb.service.CompanyService;

public class ComputerDTOMapper {
	
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private CompanyService companyService = new CompanyService();
	
	public ComputerDTO createComputerDTO(String name, String id, String introduced, String discontinued, CompanyDTO company) {
		ComputerDTO computerDTO = new ComputerDTO();
		if (!name.isBlank()) {
			computerDTO.setName(name);
		}
		if (!id.isBlank()) {
			computerDTO.setId(Long.parseLong(id));
		}
		
		if (!introduced.isBlank()) {
			computerDTO.setIntroduced(introduced);
		}
		if (!discontinued.isBlank()) {
			computerDTO.setDiscontinued(discontinued);
		}
		
		if (company != null) {
			computerDTO.setCompany(company);
		}
		
		return computerDTO;
	}

	public ComputerDTO computerToDTO(Computer computer) {
		ComputerDTO computerDTO = new ComputerDTO();
		if (computer.getName() != null) {
			computerDTO.setName(computer.getName());
		}
		
		computerDTO.setId(computer.getID());
		
		if (computer.getIntroductionDate() != null) {
			computerDTO.setIntroduced(computer.getIntroductionDate().toString());
		}
		if (computer.getDiscontinuationDate() != null) {
			computerDTO.setDiscontinued(computer.getDiscontinuationDate().toString());
		}
		if (computer.getCompany() != null) {
			CompanyDTOMapper companyDTOMapper = new CompanyDTOMapper();
			computerDTO.setCompany(companyDTOMapper.companyToDTO(computer.getCompany()));
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
		
		if (!computerDTO.getIntroduced().isEmpty()) {
			computerbuilder = computerbuilder.withIntroduced(LocalDate.parse(computerDTO.getIntroduced(), df));
		}
		if (!computerDTO.getDiscontinued().isEmpty()) {
			computerbuilder = computerbuilder.withDiscontinued(LocalDate.parse(computerDTO.getDiscontinued(), df));
		}
		
		if (computerDTO.getCompany() != null) {
			Optional<Company> company = companyService.getCompany(computerDTO.getCompany().getName());
			if (company.isPresent()) {
				computerbuilder = computerbuilder.withCompany(company.get());
			}
		}
		
		return computerbuilder.build();
	}

}
