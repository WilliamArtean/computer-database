package com.excilys.mantegazza.cdb.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.model.Computer.ComputerBuilder;

@Component
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
		
		ComputerBuilder builder = new Computer.ComputerBuilder(rs.getString("computer.name"))
				.withID(rs.getLong("id"));
		if (rs.getString("introduced") != null) {
			builder.withIntroduced(rs.getTimestamp("introduced").toLocalDateTime().toLocalDate());
		}
		if (rs.getString("discontinued") != null) {
			builder.withDiscontinued(rs.getTimestamp("discontinued").toLocalDateTime().toLocalDate());
		}
		if (rs.getString("company_id") != null) {
			builder.withCompany(new Company.CompanyBuilder(rs.getString("company.name")).withID(rs.getLong("company_id")).build());
		}
		
		Computer computer = builder.build();
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
			ComputerBuilder builder = new Computer.ComputerBuilder(rs.getString("computer.name"))
					.withID(rs.getLong("id"));
			if (rs.getString("introduced") != null) {
				builder.withIntroduced(rs.getTimestamp("introduced").toLocalDateTime().toLocalDate());
			}
			if (rs.getString("discontinued") != null) {
				builder.withDiscontinued(rs.getTimestamp("discontinued").toLocalDateTime().toLocalDate());
			}
			if (rs.getString("company_id") != null) {
				builder.withCompany(new Company.CompanyBuilder(rs.getString("company.name")).withID(rs.getLong("company_id")).build());
			}
			Computer computer = builder.build();
			computers.add(computer);
		}
		return computers;
	}
	
}
