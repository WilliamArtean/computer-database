package com.excilys.mantegazza.cdb.controllers;

import java.util.List;
import java.util.Optional;

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

import com.excilys.mantegazza.cdb.Computer;
import com.excilys.mantegazza.cdb.ComputerService;
import com.excilys.mantegazza.cdb.dto.ComputerDto;
import com.excilys.mantegazza.cdb.mappers.ComputerDTOMapper;
import com.google.common.base.Preconditions;

@RestController
@RequestMapping(value = "/computer", produces = MediaType.APPLICATION_JSON_VALUE)
public class ComputerController {
	
	private ComputerService service;
	private ComputerDTOMapper mapper;
	
	public ComputerController(ComputerService service) {
		super();
		this.service = service;
	}
	
	
	@GetMapping
	public List<ComputerDto> findAll() {
		return mapper.computersToDTOArray(service.getAllComputers());
	}
	
	@GetMapping(value = "/{id}")
	public ComputerDto findById(@PathVariable("id") Long id) {
		ComputerDto dto = null;
		Optional<Computer> computer = service.getComputer(id.longValue());
		
		if (computer.isPresent()) {
			dto = mapper.computerToDTO(computer.get());
		}
		
		return dto;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody ComputerDto dto) {
		Preconditions.checkNotNull(dto);
		service.create(mapper.dtoToComputer(dto));
	}
	
	@PutMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void update(@PathVariable( "id" ) Long id, @RequestBody ComputerDto dto) {
		Preconditions.checkNotNull(dto);
		if (service.getComputer(id.longValue()).isPresent()) {
			service.update(id.longValue(), mapper.dtoToComputer(dto));
		}
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

}
