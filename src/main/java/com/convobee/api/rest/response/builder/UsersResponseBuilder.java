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
			LocalDateTime createdAt,List<String> interests){
		UsersResponse user = UsersResponse.builder()
								.userName(userName)
								.nickName(nickName)
								.mailId(mailId)
								.password(password)
								.country(country)
								.city(city)
								.educationLevel(educationLevel)
								.interests(interests)
								.createdAt(createdAt).build();
		return user;
		
	}
}
