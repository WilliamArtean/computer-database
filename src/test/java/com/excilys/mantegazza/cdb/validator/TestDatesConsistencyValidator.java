package com.excilys.mantegazza.cdb.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import validator.DatesConsistencyValidator;

public class TestDatesConsistencyValidator {

	private DatesConsistencyValidator validator;
	
	private final LocalDate earlierDate = LocalDate.of(2000, 10, 31);
	private final LocalDate laterDate = LocalDate.of(2021, 05, 25);
	
	@Before
	public void initValidator() {
		validator = new DatesConsistencyValidator();
	}
	
	@Test
	public void withEmptyDates() {
		assertTrue(validator.areDatesConsistent(Optional.empty(), Optional.empty()));
	}
	
	@Test
	public void withIntroducedOnly() {
		assertTrue(validator.areDatesConsistent(Optional.of(earlierDate), Optional.empty()));
	}
	
	@Test
	public void withDiscontinuedOnly() {
		assertTrue(validator.areDatesConsistent(Optional.empty(), Optional.of(laterDate)));
	}
	
	@Test
	public void withCorrectDates() {
		assertTrue(validator.areDatesConsistent(Optional.of(earlierDate), Optional.of(laterDate)));
	}
	
	@Test
	public void withInconsistentDates() {
		assertFalse(validator.areDatesConsistent(Optional.of(laterDate), Optional.of(earlierDate)));
	}
	
}
