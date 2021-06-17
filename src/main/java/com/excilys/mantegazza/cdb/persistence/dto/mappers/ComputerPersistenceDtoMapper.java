package com.excilys.mantegazza.cdb.persistence.dto.mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.model.Computer.ComputerBuilder;
import com.excilys.mantegazza.cdb.persistence.dto.ComputerPersistenceDto;

@Component
public class ComputerPersistenceDtoMapper {

	private CompanyPersistenceDtoMapper companyDtoMapper;
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
	

	public ComputerPersistenceDtoMapper(CompanyPersistenceDtoMapper companyDtoMapper) {
		this.companyDtoMapper = companyDtoMapper;
	}

	
	public Optional<ComputerPersistenceDto> computerToDto(Computer computer) {
		if (computer == null) {
			return Optional.empty();
		}
		
		ComputerPersistenceDto computerDto = new ComputerPersistenceDto();
		computerDto.setId(computer.getID());
		computerDto.setName(computer.getName());
		
		if (computer.getIntroductionDate() != null) {
			computerDto.setIntroduced(computer.getIntroductionDate().toString());
		}
		if (computer.getDiscontinuationDate() != null) {
			computerDto.setDiscontinued(computer.getDiscontinuationDate().toString());
		}
		companyDtoMapper.mapToDto(computer.getCompany()).ifPresent(
			companyDto -> computerDto.setCompanyDto(companyDto)
		);
		
		return Optional.of(computerDto);
	}
	
	public ArrayList<ComputerPersistenceDto> computersToDTOArray(ArrayList<Computer> computers) {
		ArrayList<ComputerPersistenceDto> computerDTOArray = new ArrayList<ComputerPersistenceDto>();
		computers.stream().forEach(computer -> {
			computerToDto(computer).ifPresent(c -> computerDTOArray.add(c));
		});
			
		return computerDTOArray;
	}
	
	public Optional<Computer> dtoToComputer(ComputerPersistenceDto computerDto) {
		if (computerDto == null) {
			return Optional.empty();
		}
		
		ComputerBuilder computerBuilder = new Computer.ComputerBuilder();
		computerBuilder.withID(computerDto.getId());
		computerBuilder.withName(computerDto.getName());
		
		if (computerDto.getIntroduced() != null) {
			computerBuilder.withIntroduced(LocalDate.parse(computerDto.getIntroduced(), df));
		}
		if (computerDto.getDiscontinued() != null) {
			computerBuilder.withDiscontinued(LocalDate.parse(computerDto.getDiscontinued(), df));
		}
		companyDtoMapper.dtoToCompany(computerDto.getCompanyDto()).ifPresent(
			company -> computerBuilder.withCompany(company)
		);
		
		return Optional.of(computerBuilder.build());
	}
	
	public ArrayList<Computer> dtosToComputerArray(ArrayList<ComputerPersistenceDto> computerDtos) {
		ArrayList<Computer> computerDTOArray = new ArrayList<Computer>();
		computerDtos.stream().forEach(dto -> {
			dtoToComputer(dto).ifPresent(c -> computerDTOArray.add(c));
		});
			
		return computerDTOArray;
	}
	
}
