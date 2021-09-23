package com.convobee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.data.entity.Users;
import com.convobee.data.repository.UsersRepo;

@Service
public class UsersService {

	@Autowired
	UsersRepo usersRepo;
	
	public void createUser(Users user) throws Exception
	{
		usersRepo.save(user);
	}
}
