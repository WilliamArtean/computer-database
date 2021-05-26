package com.excilys.mantegazza.cdb.validator;

import java.time.LocalDate;
import java.util.Optional;

public class DatesConsistencyValidator {

	/**
	 * Checks if the introduced date is before the discontinued date.
	 * @param introduced An Optional containing the introduced date
	 * @param discontinued An Optional containing the discontinued date
	 * @return true if the discontinued date is after the introduced date, or if at least one of the dates is null.
	 * false if the discontinued date is after the introduced date.
	 */
	public boolean areDatesConsistent(Optional<LocalDate> introduced, Optional<LocalDate> discontinued) {
		if (introduced.isEmpty() || discontinued.isEmpty()) {
			return true;
		}
		return introduced.get().isBefore(discontinued.get());
	}
	
}
