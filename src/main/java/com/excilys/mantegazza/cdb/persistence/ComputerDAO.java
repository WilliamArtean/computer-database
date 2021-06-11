package com.excilys.mantegazza.cdb.persistence;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.mantegazza.cdb.enums.Order;
import com.excilys.mantegazza.cdb.enums.OrderBy;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.persistence.mappers.ComputerMapper;
import com.excilys.mantegazza.cdb.persistence.mappers.ComputerRowMapper;
import com.excilys.mantegazza.cdb.service.Page;

@Repository
public class ComputerDAO {
	
	private final String queryGetByID = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.id = :computerId";
	private final String queryGetByName = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.name = :computerName";
	private final String queryDeleteByID = "DELETE FROM computer WHERE id = :computerId";
	private final String queryDeleteMultipleIds = "DELETE FROM computer WHERE id IN (:ids)";
	private final String queryDeleteByName = "DELETE FROM computer WHERE name = :computerName";
	private final String queryCreate = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (:computerName, :introduced, :discontinued, :companyId)";
	private final String queryUpdate = "UPDATE computer SET name = :computerName, introduced = :introduced, discontinued = :discontinued, company_id = :companyId WHERE computer.id = :computerId";
	private final String queryOrderedLimitedSearch = "SELECT computer.id, computer.name, introduced, discontinued, company_id, company.name FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.name LIKE :search OR company.name LIKE :search ORDER BY %s %s LIMIT :limit OFFSET :offset";
	private final String queryCount = "SELECT COUNT(computer.id) AS rowcount FROM computer LEFT JOIN company on computer.company_id = company.id WHERE computer.name LIKE :search OR company.name LIKE :search ORDER BY computer.id";

	ComputerMapper computerMapper;
	private ComputerRowMapper rowMapper;
	private NamedParameterJdbcTemplate npJdbcTemplate;
	
	public ComputerDAO(NamedParameterJdbcTemplate npJdbcTemplate, ComputerRowMapper rowMapper, ComputerMapper computerMapper) {
		this.npJdbcTemplate = npJdbcTemplate;
		this.rowMapper = rowMapper;
		this.computerMapper = computerMapper;
	}

	
	public Optional<Computer> getByID(long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("computerId", id);
		Computer computer = (Computer) npJdbcTemplate.queryForObject(queryGetByID, namedParameters, rowMapper);
		
		Optional<Computer> optComputer = Optional.empty();
		if (computer != null) {
			optComputer = Optional.of(computer);
		}
		return optComputer;
	}
	
	public Optional<Computer> getByName(String name) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("computerName", name);
		Computer computer = (Computer) npJdbcTemplate.queryForObject(queryGetByName, namedParameters, rowMapper);
		
		Optional<Computer> optComputer = Optional.empty();
		if (computer != null) {
			optComputer = Optional.of(computer);
		}
		return optComputer;
	}
	
	public ArrayList<Computer> getAll() {
		String query = String.format(queryOrderedLimitedSearch, OrderBy.none.getSQLKeyword(), Order.ascending.getSQLKeyword());
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("search", "%%");
		namedParameters.addValue("limit", Long.MAX_VALUE);
		namedParameters.addValue("offset", 0);
		
		return (ArrayList<Computer>) npJdbcTemplate.query(query, namedParameters, rowMapper);
	}
	
	public ArrayList<Computer> getSelection(Page page) {
		String query = String.format(queryOrderedLimitedSearch, OrderBy.none.getSQLKeyword(), Order.ascending.getSQLKeyword());
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("search", "%" + page.getSearchTerm() + "%");
		namedParameters.addValue("limit", page.getItemsPerPage());
		namedParameters.addValue("offset", page.getRowOffset());
		
		return (ArrayList<Computer>) npJdbcTemplate.query(query, namedParameters, rowMapper);
	}
	
	public int getCount() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("search", "%%");
		return npJdbcTemplate.queryForObject(queryCount, namedParameters, Integer.class);
	}
	
	public int getCount(Page page) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("search", "%" + page.getSearchTerm() + "%");
		return npJdbcTemplate.queryForObject(queryCount, namedParameters, Integer.class);
	}
	
	
	public void create(Computer computer) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters = computerMapper.computerToParameterSource(computer, namedParameters);
		
		npJdbcTemplate.update(queryCreate, namedParameters);
	}
	
	public void update(long computerId, Computer updatedComputer) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("computerId", computerId);
		namedParameters = computerMapper.computerToParameterSource(updatedComputer, namedParameters);

		npJdbcTemplate.update(queryUpdate, namedParameters);
	}
	
	public void delete(long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("computerId", id);
		npJdbcTemplate.update(queryDeleteByID, namedParameters);
	}
	
	public void delete(ArrayList<Long> idsToDelete) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("ids", idsToDelete);
		npJdbcTemplate.update(queryDeleteMultipleIds, namedParameters);
	}
	
	public void delete(String name) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("computerName", name);
		npJdbcTemplate.update(queryDeleteByName, namedParameters);
	}
	
}
