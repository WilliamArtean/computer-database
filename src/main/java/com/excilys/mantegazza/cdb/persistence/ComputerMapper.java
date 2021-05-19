package com.excilys.mantegazza.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;

public class ComputerMapper {

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
			computer.setCompany(new Company(rs.getString("company.name")));
		}
		
		return Optional.of(computer);
	}
	
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
				computer.setCompany(new Company(rs.getString("company.name")));
			}
			computers.add(computer);
		}
		return computers;
	}
	
}
