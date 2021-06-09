package com.excilys.mantegazza.cdb.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.model.Computer.ComputerBuilder;

@Component
public class ComputerRowMapper implements RowMapper<Computer> {

	@Override
	public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
		ComputerBuilder computerBuilder = new Computer.ComputerBuilder();
		computerBuilder.withName(rs.getString("computer.name"));
		computerBuilder.withID(rs.getLong("computer.id"));
		if (rs.getString("introduced") != null) {
			computerBuilder.withIntroduced(rs.getTimestamp("introduced").toLocalDateTime().toLocalDate());
		}
		if (rs.getString("discontinued") != null) {
			computerBuilder.withDiscontinued(rs.getTimestamp("discontinued").toLocalDateTime().toLocalDate());
		}
		if (rs.getString("company_id") != null) {
			computerBuilder.withCompany(new Company.CompanyBuilder(rs.getString("company.name")).withID(rs.getLong("company_id")).build());
		}
		return computerBuilder.build();
	}

}
