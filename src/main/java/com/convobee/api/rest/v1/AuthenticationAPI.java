package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.AuthenticationRequest;
import com.convobee.api.rest.response.AuthenticationResponse;
import com.convobee.authentication.AuthUserDetails;
import com.convobee.authentication.AuthUserDetailsService;
import com.convobee.utils.JWTUtil;
import com.convobee.utils.UserUtil;

@RestController
public class AuthenticationAPI {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	AuthUserDetailsService userDetailsService;
	
	@Autowired
	JWTUtil jwtUtil;
	
	@Autowired
	UserUtil userUtil;
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
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
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello(HttpServletRequest request)
	{
		System.out.println("Auth API User ID = " + userUtil.getLoggedInUserId(request));
		return "Worked";
	}
}
