package com.excilys.mantegazza.cdb.dto.mappers;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.excilys.mantegazza.cdb.dto.CompanyDTO;
import com.excilys.mantegazza.cdb.model.Company;

@Component
public class CompanyDTOMapper {
	
	public CompanyDTO createCompanyDTO(String id, String name) {
		CompanyDTO companyDTO = new CompanyDTO();
		
		if (!id.isBlank()) {
			companyDTO.setId(Long.parseLong(id));
		}
		if (!name.isBlank()) {
			companyDTO.setName(name);
		}
		
		return companyDTO;
	}
	
	public CompanyDTO companyToDTO(Company company) {
		CompanyDTO companyDTO = new CompanyDTO();
		if (company.getName() != null) {
			companyDTO.setName(company.getName());
		}
		companyDTO.setId(company.getID());
		return companyDTO;
	}
	
	public ArrayList<CompanyDTO> companiesToDTOArray(ArrayList<Company> companies) {
		ArrayList<CompanyDTO> companyDTOArray = new ArrayList<CompanyDTO>();
		for (Company company : companies) {
			companyDTOArray.add(companyToDTO(company));
		}
		return companyDTOArray;
	}
	
	public Company dtoToCompany(CompanyDTO companyDTO) {
		String nameToTransfer = null;
		if (!companyDTO.getName().isEmpty()) {
			nameToTransfer = companyDTO.getName();
		}
		long idToTransfer = companyDTO.getId();
		
		Company company = new Company.CompanyBuilder(nameToTransfer).withID(idToTransfer).build();
		return company;
	}

}
