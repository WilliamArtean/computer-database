package com.excilys.mantegazza.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;

public class ComputerMapper {

	/**
	 * Create a single computer object from a ResultSet.
	 * @param rs The ResultSet to create the Computer object from
	 * @return An Optional containing the created Computer object, or empty Optional if the ResultSet was empty
	 * @throws SQLException
	 */
	public Optional<Computer> mapToComputer(ResultSet rs) throws SQLException {
		if (!rs.isBeforeFirst()) {
			return Optional.empty();
		}
		rs.next();
		
		Computer computer = new Computer(rs.getString("computer.name"));
		computer.setID(rs.getLong("id"));
		if (rs.getString("introduced") != null) {
			computer.setIntroductionDate(rs.getTimestamp("introduced").toLocalDateTime().toLocalDate());
		}
		if (rs.getString("discontinued") != null) {
			computer.setDiscontinuationDate(rs.getTimestamp("discontinued").toLocalDateTime().toLocalDate());
		}
		if (rs.getString("company_id") != null) {
			computer.setCompany(new Company.CompanyBuilder(rs.getString("company.name")).build());
		}
		
		return Optional.of(computer);
	}
	
	/**
	 * Create a list of computer objects from a ResultSet.
	 * @param rs The ResultSet to create the Computer object list from
	 * @return An ArrayList containing the created Computer objects, empty if the ResultSet was empty
	 * @throws SQLException
	 */
	public ArrayList<Computer> mapToComputerArray(ResultSet rs) throws SQLException {
		ArrayList<Computer> computers = new ArrayList<Computer>();
		while (rs.next()) {			
			Computer computer = new Computer(rs.getString("computer.name"));
			computer.setID(rs.getLong("id"));
			if (rs.getString("introduced") != null) {
				computer.setIntroductionDate(rs.getTimestamp("introduced").toLocalDateTime().toLocalDate());
			}
			if (rs.getString("discontinued") != null) {
				computer.setDiscontinuationDate(rs.getTimestamp("discontinued").toLocalDateTime().toLocalDate());
			}
			if (rs.getString("company_id") != null) {
				computer.setCompany(new Company.CompanyBuilder(rs.getString("company.name")).build());
			}
			computers.add(computer);
		}
		return computers;
	}
	
}
