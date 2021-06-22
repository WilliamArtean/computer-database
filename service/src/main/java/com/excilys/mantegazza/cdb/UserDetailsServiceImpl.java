package com.excilys.mantegazza.cdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.excilys.mantegazza.cdb.dao.UserDao;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserDao dao;
	
	@Autowired
	public void setDao(UserDao dao) {
		this.dao = dao;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = dao.getUserDetails(username);
		
		if (user == null) {
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("USER IS NULL");
		}
		
		return user;
	}

	
	
}
