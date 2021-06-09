package com.excilys.mantegazza.cdb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excilys.mantegazza.cdb.exceptions.InconsistentDatesException;
import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.service.CompanyService;

@ExtendWith(MockitoExtension.class)
class CLIComputerMapperTest {
	
	@Mock
	private CompanyService companyService;
	@InjectMocks
	CLIComputerMapper mapperSUT = new CLIComputerMapper();

	@Test
	void testMapToComputer() throws InconsistentDatesException {
		Company testCompany = new Company.CompanyBuilder("Test Company").build();
		Computer testComputer = new Computer.ComputerBuilder()
				.withName("Test Computer")
				.withIntroduced(LocalDate.of(2000, 05, 30))
				.withDiscontinued(LocalDate.of(2020, 07, 01))
				.withCompany(testCompany)
				.build();
		
		when(companyService.getCompany(testComputer.getCompany().getName()))
			.thenReturn(Optional.of(testCompany));
		
		Computer mappedComputer = mapperSUT.cliInputToComputer(
				Optional.of(testComputer.getName()),
				Optional.of(testComputer.getIntroductionDate()),
				Optional.of(testComputer.getDiscontinuationDate()),
				Optional.of(testComputer.getCompany().getName()));
		
		assertEquals(testComputer, mappedComputer);
	}
	
	@Test
	void testMapToComputerWithEmpties() throws InconsistentDatesException {
		Computer testComputer = new Computer.ComputerBuilder()
				.withName("Test Computer Empties")
				.build();
		
		Computer mappedComputer = mapperSUT.cliInputToComputer(
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				Optional.empty());
		assertEquals(testComputer, mappedComputer);
	}
	
	@Test
	public void testMapToComputerWithNonExistingCompany() throws InconsistentDatesException {
		Computer computerWithoutCompany = new Computer.ComputerBuilder().withName("Computer Without Company").build();
		Company ghostCompany = new Company.CompanyBuilder("Non-existing company").build();
		
		when(companyService.getCompany(ghostCompany.getName())).thenReturn(Optional.empty());
		
		Computer mappedComputer = mapperSUT.cliInputToComputer(Optional.of(computerWithoutCompany.getName()), Optional.empty(), Optional.empty(), Optional.of(ghostCompany.getName()));
		assertEquals(computerWithoutCompany, mappedComputer);
	}

}
