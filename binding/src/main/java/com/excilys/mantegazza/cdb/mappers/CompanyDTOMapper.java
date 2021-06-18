package com.excilys.mantegazza.cdb.mappers;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.excilys.mantegazza.cdb.Company;
import com.excilys.mantegazza.cdb.dto.CompanyDto;

@Component
public class CompanyDTOMapper {
	
	public CompanyDto createCompanyDTO(String id, String name) {
		CompanyDto companyDTO = new CompanyDto();
		
		if (!id.isBlank()) {
			companyDTO.setId(Long.parseLong(id));
		}
		if (!name.isBlank()) {
			companyDTO.setName(name);
		}
		
		return companyDTO;
	}
	
	public CompanyDto companyToDTO(Company company) {
		CompanyDto companyDTO = new CompanyDto();
		if (company.getName() != null) {
			companyDTO.setName(company.getName());
		}
		companyDTO.setId(company.getID());
		return companyDTO;
	}
	
	public ArrayList<CompanyDto> companiesToDTOArray(ArrayList<Company> companies) {
		ArrayList<CompanyDto> companyDTOArray = new ArrayList<CompanyDto>();
		companies.stream().forEach(company -> companyDTOArray.add(companyToDTO(company)));
		return companyDTOArray;
	}
	
	public Company dtoToCompany(CompanyDto companyDTO) {
		String nameToTransfer = null;
		if (!companyDTO.getName().isEmpty()) {
			nameToTransfer = companyDTO.getName();
		}
		long idToTransfer = companyDTO.getId();
		
		Company company = new Company.CompanyBuilder(nameToTransfer).withID(idToTransfer).build();
		return company;
	}

}
