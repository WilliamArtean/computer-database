package com.excilys.mantegazza.cdb.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.excilys.mantegazza.cdb.dto.ComputerDto;
import com.excilys.mantegazza.cdb.exceptions.InconsistentDatesException;
import com.excilys.mantegazza.cdb.exceptions.InexistentNameException;

@Component
public class ComputerValidator {

	public static final String ERR_COMPUTER_NAME = "computerNameError";
	public static final String ERR_DATES = "datesError";
	public static final String ERR_PARSE_DATE = "dateParseError";
	
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public HashMap<String, String> validateComputer(ComputerDto computer) {
		HashMap<String, String> errors = new HashMap<String, String>();
		
		try {
			validateName(computer.getName());
		} catch (InexistentNameException e) {
			errors.put(ERR_COMPUTER_NAME, "The name of the computer cannot be empty.");
		}
		
		try {
			validateDates(computer.getIntroduced(), computer.getDiscontinued());
		} catch (DateTimeParseException e) {
			errors.put(ERR_PARSE_DATE, "The date format is incorrect");
		} catch (InconsistentDatesException e) {
			errors.put(ERR_DATES, "The dates are inconsistent.");
		}
		
		return errors;
	}
	
	private void validateName(String name) throws InexistentNameException {
		if ((name == null) || (name.isBlank())) {
			throw new InexistentNameException();
		}
	}
	
	private void validateDates(String introduced, String discontinued) throws InconsistentDatesException, DateTimeParseException {
		if (!introduced.isEmpty() && !discontinued.isEmpty()) {
			LocalDate introducedDate = LocalDate.parse(introduced, df);
			LocalDate discontinuedDate = LocalDate.parse(discontinued, df);
			
			if (introducedDate.isAfter(discontinuedDate)) {
				throw new InconsistentDatesException();
			}
		}
	}
	
}
