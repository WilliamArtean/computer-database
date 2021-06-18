package com.excilys.mantegazza.cdb.dao;

import java.util.ArrayList;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.excilys.mantegazza.cdb.Company;
import com.excilys.mantegazza.cdb.dto.CompanyPersistenceDto;
import com.excilys.mantegazza.cdb.dto.QCompanyPersistenceDto;
import com.excilys.mantegazza.cdb.dto.QComputerPersistenceDto;
import com.excilys.mantegazza.cdb.mappers.CompanyPersistenceDtoMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CompanyDAO implements ICompanyDao {
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;
	JPAQueryFactory queryFactory;
	CompanyPersistenceDtoMapper companyDtoMapper;
	QCompanyPersistenceDto qCompanyDto = QCompanyPersistenceDto.companyPersistenceDto;
	
	
	public CompanyDAO(SessionFactory sessionFactory, CompanyPersistenceDtoMapper companyDtoMapper) {
		this.entityManager = sessionFactory.createEntityManager();
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.companyDtoMapper = companyDtoMapper;
	}

	
	public Optional<Company> getById(long id) {
		CompanyPersistenceDto companyDto = queryFactory.selectFrom(qCompanyDto)
				.where(qCompanyDto.id.eq(id))
				.fetchOne();
		
		Optional<Company> company = companyDtoMapper.dtoToCompany(companyDto);
		return company;
	}
	
	public Optional<Company> getByName(String name) {
		CompanyPersistenceDto companyDto = queryFactory.selectFrom(qCompanyDto)
				.where(qCompanyDto.name.eq(name))
				.fetchOne();
		
		Optional<Company> company = companyDtoMapper.dtoToCompany(companyDto);
		return company;
	}

	public ArrayList<Company> getAll() {
		ArrayList<CompanyPersistenceDto> companyDtos = new ArrayList<CompanyPersistenceDto>(
			queryFactory.selectFrom(qCompanyDto).fetch()
		);
		
		ArrayList<Company> companies = companyDtoMapper.dtosTocompanyArray(companyDtos);
		return companies;
	}
	
	public int getCount() {
		return (int) queryFactory.selectFrom(qCompanyDto).fetchCount();
	}
	
	@Transactional
	public void delete(long id) {
		QComputerPersistenceDto qComputerDto = QComputerPersistenceDto.computerPersistenceDto;
		queryFactory.delete(qComputerDto)
			.where(qComputerDto.companyDto.id.eq(id))
			.execute();
		queryFactory.delete(qCompanyDto)
			.where(qCompanyDto.id.eq(id))
			.execute();
	}
	
}
