package com.excilys.mantegazza.cdb.controllers.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.excilys.mantegazza.cdb.controllers.CompanyController;
import com.excilys.mantegazza.cdb.dto.CompanyDto;

@Component
public class CompanyModelAssembler implements RepresentationModelAssembler<CompanyDto, EntityModel<CompanyDto>> {

	@Override
	public EntityModel<CompanyDto> toModel(CompanyDto company) {
		return EntityModel.of(company,
				linkTo(methodOn(CompanyController.class).findById(company.getId())).withSelfRel(),
				linkTo(methodOn(CompanyController.class).findAll()).withRel("company"));
	}
	
}
