package com.convobee.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUtil {
	@Autowired
	JWTUtil jwtUtil;
//	public int getLoggedInUserId(String jwt)
//	{
//		return jwtUtil.extractUserId(jwt);
//	}
	
	public int getLoggedInUserId(HttpServletRequest request)
	{
		final String authorizationHeader = request.getHeader("Authorization");
		String jwt = null;
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer "))
		{
			jwt = authorizationHeader.substring(7);
		}
		return jwtUtil.extractUserId(jwt);
	}
}
