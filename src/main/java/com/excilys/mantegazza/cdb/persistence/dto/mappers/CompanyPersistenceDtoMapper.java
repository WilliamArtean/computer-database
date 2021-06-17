package com.excilys.mantegazza.cdb.persistence.dto.mappers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.persistence.dto.CompanyPersistenceDto;

@Component
public class CompanyPersistenceDtoMapper {

	public Optional<CompanyPersistenceDto> mapToDto(Company company) {
		if (company == null) {
			return Optional.empty();
		}
		
		CompanyPersistenceDto companyDto = new CompanyPersistenceDto();
		companyDto.setId(company.getID());
		companyDto.setName(company.getName());
		return Optional.of(companyDto);
	}
	
	public ArrayList<CompanyPersistenceDto> companiesToDTOArray(ArrayList<Company> companies) {
		ArrayList<CompanyPersistenceDto> companyDTOArray = new ArrayList<CompanyPersistenceDto>();
		companies.stream().forEach(company -> {
			mapToDto(company).ifPresent(dto -> companyDTOArray.add(dto));
		});
		return companyDTOArray;
	}
	
	public Optional<Company> dtoToCompany(CompanyPersistenceDto companyDto) {
		if (companyDto == null) {
			return Optional.empty();
		}
		
		Company company = new Company.CompanyBuilder()
				.withID(companyDto.getId())
				.withName(companyDto.getName())
				.build();
		
		return Optional.of(company);
	}
	
	public ArrayList<Company> dtosTocompanyArray(ArrayList<CompanyPersistenceDto> companyDtos) {
		ArrayList<Company> companyDTOArray = new ArrayList<Company>();
		companyDtos.stream().forEach(dto -> {
			dtoToCompany(dto).ifPresent(company -> companyDTOArray.add(company));
		});
		return companyDTOArray;
	}
	
}
