package com.convobee.api.rest.response.builder;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.UsersResponse;

@Service
public class UsersResponseBuilder {
	public  UsersResponse buildResponse(
			String userName,String nickName,String password,String mailId,
			String country,String city,String educationLevel,
			LocalDateTime createdAt,String role, String signuptype,List<String> interests){
		UsersResponse user = UsersResponse.builder()
								.username(userName)
								.nickname(nickName)
								.mailid(mailId)
								.password(password)
								.country(country)
								.city(city)
								.educationlevel(educationLevel)
								.interests(interests)
								.createdat(createdAt)
								.role(role)
								.signuptype(signuptype).build();
		return user;
		
	}
}
