package com.convobee.authentication;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.convobee.data.entity.Users;

@Service
public class AuthUserDetails implements UserDetails{
	
	private String mailid;
	private String password;
	private List<GrantedAuthority> authorities;
	
	public AuthUserDetails(Users user)
	{
		this.mailid = user.getMailid();
		this.password = user.getPassword();
		this.authorities = Arrays.stream(user.getRole().split(","))
														.map(SimpleGrantedAuthority::new)
														.collect(Collectors.toList());
																	
	}
	public AuthUserDetails() {
		
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return mailid;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
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
		//Hardcoded as true since we do not have such usecase
		return true;
	}

}
