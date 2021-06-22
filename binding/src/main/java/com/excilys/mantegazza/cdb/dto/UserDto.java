package com.excilys.mantegazza.cdb.dto;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.excilys.mantegazza.cdb.enums.Roles;

@Entity
@Table(name = "user_table")
public class UserDto implements UserDetails {

	private static final long serialVersionUID = -3849046612028329973L;

	@Id
	@GeneratedValue
	private long id;
	@Column(name = "email", nullable = false, unique = true)
	private String username;
	private String password;
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Roles role;
	private boolean deleted;
	
	
	public UserDto() {
		
	}
	
	public UserDto(long id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		GrantedAuthority authority = new SimpleGrantedAuthority(getRole().toString());
		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(authority);
		return authorities;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
