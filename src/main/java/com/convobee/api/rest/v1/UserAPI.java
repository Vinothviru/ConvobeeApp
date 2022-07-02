package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.UsersRequest;
import com.convobee.api.rest.response.BaseResponse;
import com.convobee.service.UsersService;

@RestController
@CrossOrigin
public class UserAPI {
	
	@Autowired
	UsersService usersService;

	@RequestMapping(value = "/getuserdetails", method = RequestMethod.GET)
	public ResponseEntity getUserDetails(HttpServletRequest request) throws Exception{
		return  new BaseResponse().getResponse( ()-> usersService.getUserDetails(request));
		//return usersService.getUserDetails(request);
	}
	
	@RequestMapping(value = "/updateuserdetails", method = RequestMethod.PATCH)
	public ResponseEntity updateUserDetails(@RequestBody UsersRequest request) throws Exception{
		return  new BaseResponse().getResponse( ()-> usersService.updateUserDetails(request));
		//return usersService.getUserDetails(request);
	}
	
	@RequestMapping(value = "/changepassword", method = RequestMethod.PATCH)
	public ResponseEntity changePassword(@RequestBody UsersRequest request) throws Exception{
		return  new BaseResponse().getResponse( ()-> usersService.changePassword(request));
	}
	
	@RequestMapping(value = "/getjwtexpiration", method = RequestMethod.GET)
	public ResponseEntity getJWTExpiration(HttpServletRequest request) throws Exception{
		return  new BaseResponse().getResponse( ()-> usersService.isUserJWTGoingToExpire(request));
	}
}
