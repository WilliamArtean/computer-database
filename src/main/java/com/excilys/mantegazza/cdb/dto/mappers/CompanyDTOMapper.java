package com.excilys.mantegazza.cdb.dto.mappers;

import com.excilys.mantegazza.cdb.dto.CompanyDTO;
import com.excilys.mantegazza.cdb.model.Company;

public class CompanyDTOMapper {
	
	public CompanyDTO companyToDTO(Company company) {
		CompanyDTO companyDTO = new CompanyDTO();
		if (company.getName() != null) {
			companyDTO.setName(company.getName());
		}
		companyDTO.setId(company.getID());
		return companyDTO;
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
