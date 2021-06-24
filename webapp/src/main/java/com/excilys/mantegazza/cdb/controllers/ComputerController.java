package com.excilys.mantegazza.cdb.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.excilys.mantegazza.cdb.Computer;
import com.excilys.mantegazza.cdb.ComputerService;
import com.excilys.mantegazza.cdb.controllers.assemblers.ComputerModelAssembler;
import com.excilys.mantegazza.cdb.dto.ComputerDto;
import com.excilys.mantegazza.cdb.mappers.ComputerDTOMapper;
import com.google.common.base.Preconditions;

@RestController
@RequestMapping(value = "/computer", produces = MediaType.APPLICATION_JSON_VALUE)
public class ComputerController {
	
	private ComputerService service;
	private ComputerDTOMapper mapper;
	private ComputerModelAssembler assembler;
	
	
	public ComputerController(ComputerService service, ComputerDTOMapper mapper, ComputerModelAssembler assembler) {
		this.service = service;
		this.mapper = mapper;
		this.assembler = assembler;
	}


	@GetMapping
	public CollectionModel<EntityModel<ComputerDto>> findAll() {
		List<EntityModel<ComputerDto>> computers = mapper.computersToDTOArray(service.getAllComputers()).stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(computers,
				linkTo(methodOn(ComputerController.class).findAll()).withSelfRel());
	}
	
	@GetMapping(value = "/{id}")
	public EntityModel<ComputerDto> findById(@PathVariable("id") Long id) {
		Computer computer = service.getComputer(id.longValue())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Computer not found"));
		
		ComputerDto dto = mapper.computerToDTO(computer);
		
		return assembler.toModel(dto);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody ComputerDto dto) {
		Preconditions.checkNotNull(dto);
		service.create(mapper.dtoToComputer(dto));
	}
	
	@PutMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@PathVariable("id") Long id, @RequestBody ComputerDto dto) {
		Preconditions.checkNotNull(dto);
		
		service.getComputer(id.longValue())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Computer to update not found"));
		
		service.update(id.longValue(), mapper.dtoToComputer(dto));
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

}
