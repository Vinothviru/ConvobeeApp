package com.convobee.authentication.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.convobee.authentication.AuthUserDetails;
import com.convobee.authentication.AuthUserDetailsService;
import com.convobee.constants.Constants;
import com.convobee.service.UsersService;
import com.convobee.utils.JWTUtil;

@Component
public class JwtResquestFilter extends OncePerRequestFilter{

	@Autowired
	AuthUserDetailsService userDetailsService;
	
	@Autowired
	JWTUtil jwtUtil;
	
	@Autowired
	UsersService usersService;
		
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader(Constants.AUTHORIZATION);
		
		String username = null;
		String jwt = null;
		AuthUserDetails userDetails = null;
		if(authorizationHeader!=null && authorizationHeader.startsWith(Constants.BEARER))
		{
			jwt = authorizationHeader.substring(7);
			username = jwtUtil.extractUsername(jwt);
			userDetails = this.userDetailsService.loadUserByUsername(username);
			if(!userDetails.isEnabled()) {
				throw new IOException(Constants.INACTIVE_USER);
			}
			/* Already checked in DefaultJwtsParser, so this seems to be redundant check. Let it be, Will check later and remove this. */
			if(jwtUtil.isTokenExpired(jwt)) {
				throw new IOException(Constants.JWT_EXPIRED);
			}
			/* This check will be costly for every request, need to check whether this is needed or not */
			if(usersService.isBannedUser(jwtUtil.extractUserId(jwt))) {
				throw new IOException(Constants.BANNED_USER);
			}
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
		{
			if(jwtUtil.validateToken(jwt, userDetails)) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities()
					);
			usernamePasswordAuthenticationToken.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request)
					);
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			//System.out.println("USER ID via AuthDetails in = JwtResquestFilter" + userDetails.getUserid());
			}
		}
		chain.doFilter(request, response);
	}
	
}
