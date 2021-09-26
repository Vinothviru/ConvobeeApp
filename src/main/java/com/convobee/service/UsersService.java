package com.convobee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.data.entity.Interests;
import com.convobee.data.entity.Users;
import com.convobee.data.repository.InterestsRepo;
import com.convobee.data.repository.UsersRepo;

@Service
public class UsersService {

	@Autowired
	UsersRepo usersRepo;
	
	@Autowired
	InterestsRepo interestsRepo; 
	
	public void createUser(Users user) throws Exception
	{
		usersRepo.save(user);
	}
	
	public void createInterestsForUser(Interests interests) throws Exception
	{
		interestsRepo.save(interests);
	}
	
}
