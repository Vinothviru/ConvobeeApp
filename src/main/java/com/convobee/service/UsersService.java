package com.convobee.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.AuthenticationRequest;
import com.convobee.api.rest.response.UsersResponse;
import com.convobee.api.rest.response.builder.UsersResponseBuilder;
import com.convobee.authentication.AuthUserDetails;
import com.convobee.authentication.AuthUserDetailsService;
import com.convobee.constants.Constants;
import com.convobee.data.entity.Interests;
import com.convobee.data.entity.Users;
import com.convobee.data.repository.InterestsRepo;
import com.convobee.data.repository.UsersRepo;
import com.convobee.utils.JWTUtil;
import com.convobee.utils.UserUtil;

@Transactional
@Service
public class UsersService {

	@Autowired
	UsersRepo usersRepo;
	
	@Autowired
	InterestsRepo interestsRepo; 
	
	@Autowired
	UserUtil userUtil;
	
	@Autowired
	UsersResponseBuilder usersResponseBuilder;
	
	@Autowired
	AuthUserDetailsService authUserDetailsService;
	
	@Autowired
	JWTUtil jwtUtil;
	
	public void createUser(Users user) throws Exception
	{
		usersRepo.save(user);
	}
	
	public void createInterestsForUser(List<Interests> interests) throws Exception
	{
		interestsRepo.saveAll(interests);
	}

	public UsersResponse getUserDetails(HttpServletRequest request) throws Exception{
		if(request==null) {
			throw new Exception(Constants.USER_TRYING_TO_ACCESS_IRRELEVANT_DATA);
		}
		int userId = userUtil.getLoggedInUserId(request);
		LinkedList<Object[]> userDetails = usersRepo.findUserDetailsByUserid(userId);
		List<String> interests = usersRepo.findInterestsByUserid(userId);
		UsersResponse usersResponse = null;
		usersResponse = usersResponseBuilder.buildResponse(userDetails.get(0)[0].toString(),
				userDetails.get(0)[1].toString(), userDetails.get(0)[2].toString(), 
				userDetails.get(0)[3].toString(), userDetails.get(0)[4].toString(), 
				userDetails.get(0)[5].toString(), userDetails.get(0)[6].toString(), LocalDateTime.parse(userDetails.get(0)[7].toString().replace(' ', 'T')), interests); 
		return usersResponse;
	}
	
	/* Verifying whether the requesting user and the data accessing user are same */
	public boolean isValidUser(HttpServletRequest request, Users user) {
		int loggedinUserId = userUtil.getLoggedInUserId(request);
		int dataAccessingUserId = user.getUserid();
		if(loggedinUserId==dataAccessingUserId) {
			return true;
		}
		return false;
	}
	
	/* Verifying whether the logged in user is banned or not */
	public boolean isBannedUser(int userId) {
		return usersRepo.findById(userId).get().isIsuserbanned();
	}
	
	
	/* Verifying whether the user name and password is correct or not */
	public AuthUserDetails authenticate(AuthenticationRequest authenticationRequest) throws Exception{
		AuthUserDetails authUserDetails = authUserDetailsService.loadUserByUsername(authenticationRequest.getMailid());
		if(authUserDetails==null) {
			throw new Exception(Constants.NO_SUCH_USER);
		}
		if(!(authUserDetails.getPassword().equals(authenticationRequest.getPassword()))) {
			throw new Exception(Constants.INCORRECT_PASSWORD);
		}
		if(isBannedUser(usersRepo.findByMailid(authUserDetails.getUsername()).get().getUserid())) {
			throw new IOException(Constants.BANNED_USER);
		}
		if(!authUserDetails.isEnabled()) {
			throw new IOException(Constants.INACTIVE_USER);
		}
		return authUserDetails;
	}
	
	public boolean isUserJWTGoingToExpire(HttpServletRequest request) {
		String jwt = jwtUtil.extractJWT(request);
		Date expDate = jwtUtil.extractExpiration(jwt);
		Date currentDate = new Date();
		long diff = expDate.getTime()-currentDate.getTime();
		long difference_In_Hours = (diff/ (1000 * 60 * 60)) % 24;
		if(difference_In_Hours<2) {
			return true;
		}
		return false;
	}
}
