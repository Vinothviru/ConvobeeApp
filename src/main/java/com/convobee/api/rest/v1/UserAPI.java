package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.response.UsersResponse;
import com.convobee.service.UsersService;

@RestController
public class UserAPI {
	
	@Autowired
	UsersService usersService;

	@RequestMapping(value = "/getuserdetails", method = RequestMethod.GET)
	public UsersResponse getUserDetails(HttpServletRequest request) throws Exception{
		return usersService.getUserDetails(request);
	}
}
