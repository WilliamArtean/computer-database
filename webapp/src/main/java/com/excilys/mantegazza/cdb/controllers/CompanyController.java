package com.excilys.mantegazza.cdb.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.excilys.mantegazza.cdb.Company;
import com.excilys.mantegazza.cdb.CompanyService;
import com.excilys.mantegazza.cdb.controllers.assemblers.CompanyModelAssembler;
import com.excilys.mantegazza.cdb.dto.CompanyDto;
import com.excilys.mantegazza.cdb.mappers.CompanyDTOMapper;

@RestController
@RequestMapping(value = "/company", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyController {

	private CompanyService service;
	private CompanyDTOMapper mapper;
	private CompanyModelAssembler assembler;
	
	
	public CompanyController(CompanyService service, CompanyDTOMapper mapper, CompanyModelAssembler assembler) {
		this.service = service;
		this.mapper = mapper;
		this.assembler = assembler;
	}

	
	@GetMapping
	public CollectionModel<EntityModel<CompanyDto>> findAll() {
		List<EntityModel<CompanyDto>> companies = mapper.companiesToDTOArray(service.getAllCompanies()).stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(companies,
				linkTo(methodOn(CompanyController.class).findAll()).withSelfRel());
	}
	
	@GetMapping(value = "/{id}")
	public EntityModel<CompanyDto> findById(@PathVariable("id") Long id) {
		Company company = service.getCompany(id.longValue())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
		
		CompanyDto dto = mapper.companyToDTO(company);
		
		return assembler.toModel(dto);
	}
	
}
