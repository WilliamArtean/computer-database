package com.excilys.mantegazza.cdb.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.excilys.mantegazza.cdb.Company;
import com.excilys.mantegazza.cdb.CompanyService;
import com.excilys.mantegazza.cdb.dto.CompanyDto;
import com.excilys.mantegazza.cdb.mappers.CompanyDTOMapper;

@RestController
@RequestMapping(value = "/company", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {

	private CompanyService service;
	private CompanyDTOMapper mapper;
	
	
	public CompanyController(CompanyService service, CompanyDTOMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}
	
	
	@GetMapping
	public List<CompanyDto> findAll() {
		return mapper.companiesToDTOArray(service.getAllCompanies());
	}
	
	@GetMapping(value = "/{id}")
	public CompanyDto findById(@PathVariable("id") Long id) {
		CompanyDto dto = null;
		Optional<Company> company = service.getCompany(id.longValue());
		
		if (company.isPresent()) {
			dto = mapper.companyToDTO(company.get());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
		}
		
		return dto;
	}
	
}
