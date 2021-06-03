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
	
	public ComputerDTO createComputerDTO(String name, String id, String introduced, String discontinued, long companyId) {
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
		
		if (companyId != 0) {
			computerDTO.setCompanyId(companyId);
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
			computerDTO.setCompanyId(computer.getCompany().getID());
			computerDTO.setCompanyName(computer.getCompany().getName());
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
		computerbuilder.withID(computerDTO.getId());
		if (!computerDTO.getName().isEmpty()) {
			computerbuilder.withName(computerDTO.getName());
		}
		
		if (!computerDTO.getIntroduced().isEmpty()) {
			computerbuilder.withIntroduced(LocalDate.parse(computerDTO.getIntroduced(), df));
		}
		if (!computerDTO.getDiscontinued().isEmpty()) {
			computerbuilder.withDiscontinued(LocalDate.parse(computerDTO.getDiscontinued(), df));
		}
		
		if (computerDTO.getCompanyId() != 0) {
			Optional<Company> company = companyService.getCompany(computerDTO.getCompanyId());
			if (company.isPresent()) {
				computerbuilder.withCompany(company.get());
			}
		}
		
		//TODO Ajouter validation back
		return computerbuilder.build();
	}

}
