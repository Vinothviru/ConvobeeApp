package com.convobee.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.UsersResponse;
import com.convobee.api.rest.response.builder.UsersResponseBuilder;
import com.convobee.data.entity.Interests;
import com.convobee.data.entity.Users;
import com.convobee.data.repository.InterestsRepo;
import com.convobee.data.repository.UsersRepo;
import com.convobee.utils.UserUtil;

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
	
	public void createUser(Users user) throws Exception
	{
		usersRepo.save(user);
	}
	
	public void createInterestsForUser(List<Interests> interests) throws Exception
	{
		interestsRepo.saveAll(interests);
	}

	public UsersResponse getUserDetails(HttpServletRequest request) {
		int userId = userUtil.getLoggedInUserId(request);
		LinkedList<Object[]> userDetails = usersRepo.findUserDetailsByUserid(userId);
		List<String> interests = usersRepo.findInterestsByUserid(userId);
		int size = userDetails.size();
		UsersResponse usersResponse = null;
		for(int i = 0; i<size ;i++) {
			usersResponse = usersResponseBuilder.buildResponse(userDetails.get(0)[0].toString(),
					userDetails.get(0)[1].toString(), userDetails.get(0)[2].toString(), 
					userDetails.get(0)[3].toString(), userDetails.get(0)[4].toString(), 
					userDetails.get(0)[5].toString(), userDetails.get(0)[6].toString(), LocalDateTime.parse(userDetails.get(0)[7].toString().replace(' ', 'T')), interests); 
		}
		return usersResponse;
	}
	
}
