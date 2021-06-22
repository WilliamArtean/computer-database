package com.excilys.mantegazza.cdb.dao;


import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.SessionFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.excilys.mantegazza.cdb.dto.QUserDto;
import com.excilys.mantegazza.cdb.dto.UserDto;
import com.querydsl.jpa.impl.JPAQueryFactory;


@Repository
public class UserDao {
	
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;
	private JPAQueryFactory queryFactory;
	private QUserDto qUserDto = QUserDto.userDto;
	
	public UserDao(SessionFactory sessionFactory) {
		this.entityManager = sessionFactory.createEntityManager();
		this.queryFactory = new JPAQueryFactory(entityManager);
	}


	public UserDetails getUserDetails(String username) {
		UserDto user = queryFactory.selectFrom(qUserDto)
				.where(qUserDto.username.eq(username))
				.fetchOne();
		return user;
	}

	public long getCount() {
		BigInteger count = (BigInteger) entityManager.createNativeQuery("SELECT count(*) FROM user_table").getSingleResult();
		return count.longValue();
	}
	
}
