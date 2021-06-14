package com.excilys.mantegazza.cdb.persistence;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.excilys.mantegazza.cdb.model.Company;
import com.excilys.mantegazza.cdb.persistence.mappers.CompanyRowMapper;

@Repository
public class CompanyDAO {
	
	private final String queryGetByID = "SELECT id, name FROM company WHERE id = :companyId";
	private final String queryGetByName = "SELECT id, name FROM company WHERE name = :companyName";
	private final String queryGetAll = "SELECT id, name FROM company";
	private final String queryGetCount = "SELECT COUNT(id) AS rowcount FROM company";
	private final String queryDeleteAssociatedComputers = "DELETE computer FROM computer LEFT JOIN company ON (computer.company_id = company.id) WHERE company.name = :companyName";
	private final String queryDeleteByName = "DELETE FROM company WHERE name = :companyName";

	private CompanyRowMapper rowMapper;
	private NamedParameterJdbcTemplate namedParametersJdbcTemplate;
	private TransactionTemplate transactionTemplate;
	
	public CompanyDAO(CompanyRowMapper rowMapper, NamedParameterJdbcTemplate npJdbcTemplate) {
		this.rowMapper = rowMapper;
		this.namedParametersJdbcTemplate = npJdbcTemplate;
	}

	
	public Optional<Company> getByID(long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("companyId", id);
		Company company = (Company) namedParametersJdbcTemplate.queryForObject(queryGetByID, namedParameters, rowMapper);
		
		Optional<Company> optCompany = Optional.empty();
		if (company != null) {
			optCompany = Optional.of(company);
		}
		return optCompany;
	}
	
	public Optional<Company> getByName(String name) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("companyName", name);
		Company company = (Company) namedParametersJdbcTemplate.queryForObject(queryGetByName, namedParameters, rowMapper);
		
		Optional<Company> optCompany = Optional.empty();
		if (company != null) {
			optCompany = Optional.of(company);
		}
		return optCompany;
	}
	
	public ArrayList<Company> getAll() {
		ArrayList<Company> companies = (ArrayList<Company>) namedParametersJdbcTemplate.query(queryGetAll, rowMapper);
		return companies;
	}
	
	public int getCount() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		return namedParametersJdbcTemplate.queryForObject(queryGetCount, namedParameters, Integer.class);
	}
	
	public void delete(String name) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("companyName", name);
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				namedParametersJdbcTemplate.update(queryDeleteAssociatedComputers, namedParameters);
				namedParametersJdbcTemplate.update(queryDeleteByName, namedParameters);
			}
		});
	}
	
}
