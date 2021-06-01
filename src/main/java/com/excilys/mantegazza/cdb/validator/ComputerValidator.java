package com.excilys.mantegazza.cdb.validator;

import java.time.LocalDate;
import java.util.HashMap;

import com.excilys.mantegazza.cdb.exceptions.InconsistentDatesException;
import com.excilys.mantegazza.cdb.exceptions.InexistentNameException;
import com.excilys.mantegazza.cdb.model.Computer;

public class ComputerValidator {

	public static final String ERR_COMPUTER_NAME = "computerNameError";
	public static final String ERR_DATES = "datesError";
	
	public HashMap<String, String> validateComputer(Computer computer) {
		HashMap<String, String> errors = new HashMap<String, String>();
		
		try {
			validateName(computer.getName());
		} catch (InexistentNameException e) {
			errors.put(ERR_COMPUTER_NAME, "The name of the computer cannot be empty.");
		}
		
		try {
			validateDates(computer.getIntroductionDate(), computer.getDiscontinuationDate());
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
	
	private void validateDates(LocalDate introduced, LocalDate discontinued) throws InconsistentDatesException {
		if (introduced != null && discontinued != null) {
			if (introduced.isAfter(discontinued)) {
				throw new InconsistentDatesException();
			}
		}
	}
	
}
