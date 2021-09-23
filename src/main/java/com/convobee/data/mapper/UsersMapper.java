package com.convobee.data.mapper;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.UsersRequest;
import com.convobee.data.entity.Users;

@Service
public class UsersMapper {
	
	public Users mapUserFromRequest(UsersRequest usersRequest) throws Exception{
		Users user = new Users();
		user.setUsername(usersRequest.getUsername());
		user.setNickname(usersRequest.getNickname());
		user.setPassword(usersRequest.getPassword());
		user.setMailid(usersRequest.getMailid());
		user.setRole(usersRequest.getRole());
		user.setCountry(usersRequest.getCountry());
		user.setCity(usersRequest.getCity());
		user.setEducationlevel(usersRequest.getEducationlevel());
		user.setProficiencylevel(usersRequest.getProficiencylevel());
		user.setSignuptype(usersRequest.getSignuptype());
		user.setReportcount(usersRequest.getReportcount());
		user.setIsfeedbackgiven(true);
		user.setCreatedat(Timestamp.valueOf("1970-01-01 00:00:01"));
		user.setModifiedat(Timestamp.valueOf("1970-01-01 00:00:01"));
		return user;
	}

}
