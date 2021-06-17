package com.excilys.mantegazza.cdb.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.excilys.mantegazza.cdb.enums.Order;
import com.excilys.mantegazza.cdb.enums.OrderBy;
import com.excilys.mantegazza.cdb.model.Computer;
import com.excilys.mantegazza.cdb.persistence.dto.ComputerPersistenceDto;
import com.excilys.mantegazza.cdb.persistence.dto.QCompanyPersistenceDto;
import com.excilys.mantegazza.cdb.persistence.dto.QComputerPersistenceDto;
import com.excilys.mantegazza.cdb.persistence.dto.mappers.ComputerPersistenceDtoMapper;
import com.excilys.mantegazza.cdb.service.Page;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ComputerDAO implements IComputerDao {
	
	//@PersistenceContext
	private EntityManager entityManager;
	private JPAQueryFactory queryFactory;
	private ComputerPersistenceDtoMapper computerDtoMapper;
	private QComputerPersistenceDto qComputerDto = QComputerPersistenceDto.computerPersistenceDto;
	private QCompanyPersistenceDto qCompanyDto = QCompanyPersistenceDto.companyPersistenceDto;
	

	public ComputerDAO(SessionFactory sessionFactory, ComputerPersistenceDtoMapper computerDtoMapper) {
		this.entityManager = sessionFactory.createEntityManager();
		this.queryFactory = new JPAQueryFactory(entityManager);
		this.computerDtoMapper = computerDtoMapper;
	}

	public Optional<Computer> getByID(long id) {
		ComputerPersistenceDto computerDto = queryFactory.selectFrom(qComputerDto)
				.where(qComputerDto.id.eq(id))
				.fetchOne();
		
		return computerDtoMapper.dtoToComputer(computerDto);
	}

	public Optional<Computer> getByName(String name) {
		ComputerPersistenceDto computerDto = queryFactory.selectFrom(qComputerDto)
				.where(qComputerDto.name.eq(name))
				.fetchOne();
		
		Optional<Computer> computer = computerDtoMapper.dtoToComputer(computerDto);
		return computer;
	}

	public ArrayList<Computer> getAll() {
		ArrayList<ComputerPersistenceDto> computerDtos = new ArrayList<ComputerPersistenceDto>(
				queryFactory.selectFrom(qComputerDto).fetch()
				);
		
		ArrayList<Computer> computers = computerDtoMapper.dtosToComputerArray(computerDtos);
		return computers;
	}

	public ArrayList<Computer> getSelection(Page page) {
		ArrayList<ComputerPersistenceDto> computerDtos = new ArrayList<ComputerPersistenceDto>(
				queryFactory.selectFrom(qComputerDto)
				.leftJoin(qCompanyDto).on(qComputerDto.companyDto.id.eq(qCompanyDto.id))
				.where(
						qComputerDto.name.like("%" + page.getSearchTerm() + "%")
					.or(
						qCompanyDto.name.like("%" + page.getSearchTerm() + "%"))
				)
				.orderBy(getOrderSpecifier(page.getOrderBy(), page.getOrder()))
				.limit(page.getItemsPerPage())
				.offset(page.getRowOffset())
				.fetch()
				);
		
		ArrayList<Computer> computers = computerDtoMapper.dtosToComputerArray(computerDtos);
		return computers;
	}

	public ArrayList<Computer> getSelection(int offset, int limit) {
		ArrayList<ComputerPersistenceDto> computerDtos = new ArrayList<ComputerPersistenceDto>(
				queryFactory.selectFrom(qComputerDto)
				.leftJoin(qCompanyDto).on(qComputerDto.companyDto.id.eq(qCompanyDto.id))
				.orderBy(qComputerDto.id.asc())
				.limit(limit)
				.offset(offset)
				.fetch()
				);
		
		ArrayList<Computer> computers = computerDtoMapper.dtosToComputerArray(computerDtos);
		return computers;
	}

	public int getCount() {
		return (int) queryFactory.selectFrom(qComputerDto).fetchCount();
	}

	public int getCount(Page page) {
		return (int) queryFactory.selectFrom(qComputerDto)
				.leftJoin(qCompanyDto).on(qComputerDto.companyDto.id.eq(qCompanyDto.id))
				.where(
						qComputerDto.name.like("%" + page.getSearchTerm() + "%")
					.or(
						qCompanyDto.name.like("%" + page.getSearchTerm() + "%"))
				)
				.fetchCount();
	}

	public void create(Computer computer) {
		computerDtoMapper.computerToDto(computer).ifPresent(dto -> {
			entityManager.getTransaction().begin();
			
			entityManager.persist(dto);
			
			entityManager.getTransaction().commit();
			entityManager.clear();
		});
	}

	public void update(long computerId, Computer updatedComputer) {
		Optional<ComputerPersistenceDto> computerDto = computerDtoMapper.computerToDto(updatedComputer);
		
		computerDto.ifPresent(dto -> {
			entityManager.getTransaction().begin();
			
			List<? extends Path<?>> paths = new ArrayList<>(Arrays.asList(
					qComputerDto.name,
					qComputerDto.introduced,
					qComputerDto.discontinued,
					qComputerDto.companyDto));
			List<?> values = new ArrayList<>(Arrays.asList(
					dto.getName(),
					dto.getIntroduced(),
					dto.getDiscontinued(),
					dto.getCompanyDto()));
			
			queryFactory.update(qComputerDto)
			.where(qComputerDto.id.eq(computerId))
			.set(paths, values)
			.execute();
			
			entityManager.getTransaction().commit();
			entityManager.clear();
		});
		
	}
	
	public void delete(long id) {
		entityManager.getTransaction().begin();
		
		queryFactory.delete(qComputerDto)
			.where(qComputerDto.id.eq(id))
			.execute();
		
		entityManager.getTransaction().commit();
		entityManager.clear();
	}

	public void delete(ArrayList<Long> idsToDelete) {
		entityManager.getTransaction().begin();
		
		queryFactory.delete(qComputerDto)
			.where(qComputerDto.id.in(idsToDelete))
			.execute();
		
		entityManager.getTransaction().commit();
		entityManager.clear();
	}
	
	
	private OrderSpecifier getOrderSpecifier(OrderBy orderBy, Order order) {
		ComparableExpressionBase orderByExpression = null;
		
		switch (orderBy) {
		case computerName:
			orderByExpression = qComputerDto.name;
			break;
		case introduced:
			orderByExpression = qComputerDto.introduced;
			break;
		case discontinued:
			orderByExpression = qComputerDto.discontinued;
			break;
		case companyName:
			orderByExpression = qCompanyDto.name;
			break;
		case none:
			orderByExpression = qComputerDto.id;
			break;
		default:
			orderByExpression = qComputerDto.id;
			break;
		}
		
		switch (order) {
		case ascending:
			return orderByExpression.asc();
		case descending:
			return orderByExpression.desc();
		default:
			return orderByExpression.asc();
		}
	}
	
}
