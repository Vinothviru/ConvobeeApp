package com.convobee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.AuthenticationRequest;
import com.convobee.api.rest.request.UsersRequest;
import com.convobee.authentication.AuthUserDetails;
import com.convobee.authentication.AuthUserDetailsService;
import com.convobee.data.entity.Users;
import com.convobee.data.mapper.UsersMapper;
import com.convobee.utils.JWTUtil;

@Service
public class AuthenticationService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	AuthUserDetailsService userDetailsService;

	@Autowired
	UsersMapper usersMapper;

	@Autowired
	UsersService usersService;

	public String signupAuthentication(UsersRequest usersRequest) throws Exception {
		Users user = usersMapper.mapUserFromRequest(usersRequest);
		usersService.createUser(user);
		final AuthUserDetails userDetails = userDetailsService.loadUserByUsername(user.getMailid());
		final String jwt = jwtUtil.generateToken(userDetails);
		return jwt;
	}

	public String loginAuthentication(AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getMailid(), authenticationRequest.getPassword())
					);
		}
		catch(BadCredentialsException e) {
			throw new Exception("Incorrect Username or Password", e);
		}
		final AuthUserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getMailid());
		final String jwt = jwtUtil.generateToken(userDetails);
		return jwt;
	}

	public String oauthLoginAuthentication(OAuth2User principal) throws Exception {
		try {
			String mailid = (principal.getAttribute("email")).toString();

			final AuthUserDetails userDetails = userDetailsService.loadUserByUsername(mailid);
			final String jwt = jwtUtil.generateToken(userDetails);
			return jwt;
		}
		catch(BadCredentialsException e) {
			throw new Exception("Incorrect Username or Password", e);
		}
	}
}
