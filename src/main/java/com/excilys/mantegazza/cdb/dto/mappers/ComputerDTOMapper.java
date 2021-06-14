package com.excilys.mantegazza.cdb.dto.mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.excilys.mantegazza.cdb.dto.ComputerDto;
import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Company.CompanyBuilder;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.model.Computer.ComputerBuilder;

@Component
public class ComputerDTOMapper {
	
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public ComputerDto createComputerDTO(String name, String id, String introduced, String discontinued, long companyId) {
		ComputerDto computerDTO = new ComputerDto();
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

	public ComputerDto computerToDTO(Computer computer) {
		ComputerDto computerDTO = new ComputerDto();
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
	
	public ArrayList<ComputerDto> computersToDTOArray(ArrayList<Computer> computers) {
		ArrayList<ComputerDto> computerDTOArray = new ArrayList<ComputerDto>();
		computers.stream().forEach(computer -> computerDTOArray.add(computerToDTO(computer)));
		return computerDTOArray;
	}
	
	public Computer dtoToComputer(ComputerDto computerDTO) {
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
			CompanyBuilder companyBuilder = new Company.CompanyBuilder().withID(computerDTO.getCompanyId());
			if (!computerDTO.getCompanyName().isEmpty()) {
				companyBuilder.withName(computerDTO.getCompanyName());
			}
			computerbuilder.withCompany(companyBuilder.build());
		}
		
		//TODO Ajouter validation back
		return computerbuilder.build();
	}

}
