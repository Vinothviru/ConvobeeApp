package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.AuthenticationRequest;
import com.convobee.api.rest.request.UsersRequest;
import com.convobee.api.rest.response.AuthenticationResponse;
import com.convobee.api.rest.response.BaseResponse;
import com.convobee.api.rest.response.builder.OauthResponseBuilder;
import com.convobee.data.entity.Users;
import com.convobee.data.repository.UsersRepo;
import com.convobee.service.AuthenticationService;
import com.convobee.utils.UserUtil;
@RestController
public class AuthenticationAPI {

	@Autowired
	UserUtil userUtil;

	@Autowired
	UsersRepo usersRepo;

	@Autowired
	AuthenticationService authenticationService; 

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<?> signupAndCreateAuthenticationToken(@RequestBody UsersRequest usersRequest) throws Exception{
		String jwt = authenticationService.signupAuthentication(usersRequest);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	/**
	 * got to know about @ModelAttribute from here -> https://stackoverflow.com/questions/26612404/spring-map-get-request-parameters-to-pojo-automatically
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ResponseEntity<?> loginAndCreateAuthenticationToken(@ModelAttribute AuthenticationRequest authenticationRequest) throws Exception{
		String jwt = authenticationService.loginAuthentication(authenticationRequest);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	/*
	 * https://spring.io/guides/tutorials/spring-boot-oauth2/
	 * */
	@RequestMapping(value = "/oauthsignup", method = RequestMethod.GET)
	public ResponseEntity<?> oauthSignup(@AuthenticationPrincipal OAuth2User principal) {
		String username = (principal.getAttribute("name")).toString();
		String mailid = (principal.getAttribute("email")).toString();
		return new BaseResponse<Object>().getResponse( ()-> new OauthResponseBuilder().buildResponse(username, mailid));
	}

	/*System.out.println((principal.getAttribute("name")).toString());
	System.out.println((principal.getAttribute("email")).toString());
	System.out.println((principal.getAttribute("email_verified")).toString());*/

	@RequestMapping(value = "/oauthlogin", method = RequestMethod.GET)
	public ResponseEntity<?> oauthLoginAndCreateAuthenticationToken(@AuthenticationPrincipal OAuth2User principal) throws Exception{
		String jwt = authenticationService.oauthLoginAuthentication(principal);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	@RequestMapping(value = "/hello/{id}", method = RequestMethod.GET)
	public String hello(HttpServletRequest request, @PathVariable int id)
	{
		Users user = usersRepo.findByUserid(id); 
		System.out.println(user);
		System.out.println("Auth API User ID = " + userUtil.getLoggedInUserId(request));
		return "Worked";
	}

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin()
	{
		return "Worked admin";
	}

}
