package com.convobee.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUtil {
	@Autowired
	JWTUtil jwtUtil;

	public int getLoggedInUserId(HttpServletRequest request)
	{
		String jwt = jwtUtil.extractJWT(request);
		return jwtUtil.extractUserId(jwt);
	}
}
