package com.excilys.mantegazza.cdb.controllers.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.excilys.mantegazza.cdb.controllers.ComputerController;
import com.excilys.mantegazza.cdb.dto.ComputerDto;

@Component
public class ComputerModelAssembler implements RepresentationModelAssembler<ComputerDto, EntityModel<ComputerDto>> {

	@Override
	public EntityModel<ComputerDto> toModel(ComputerDto computer) {
		return EntityModel.of(computer,
				linkTo(methodOn(ComputerController.class).findById(computer.getId())).withSelfRel(),
				linkTo(methodOn(ComputerController.class).findAll()).withRel("computer"));
	}

}
