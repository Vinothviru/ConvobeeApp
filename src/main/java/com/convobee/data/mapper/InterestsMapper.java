package com.convobee.data.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.UsersRequest;
import com.convobee.constants.Constants;
import com.convobee.data.entity.Interests;
import com.convobee.data.entity.Users;

@Service
public class InterestsMapper {

	public Interests mapInterestsFromRequest(Users user, UsersRequest usersRequest) throws Exception{
		List<String>  interestsList = usersRequest.getInterests();
		Interests interests = new Interests();
		interests.setUser(user);
		for(String interest : interestsList)
		{
			switch(interest)
			{
			case Constants.ART:
				interests.setArt(true);
				break;
			
			case Constants.BLOGGING:
				interests.setBlogging(true);
				break;
			
			case Constants.ACTING:
				interests.setActing(true);
				break;
			
			case Constants.SPORTS:
				interests.setSports(true);
				break;
			
			case Constants.GAMING:
				interests.setGaming(true);
				break;
			
			case Constants.TRAVELING:
				interests.setTraveling(true);
				break;
				
			case Constants.PET_CARE:
				interests.setPetcare(true);
				break;
				
			case Constants.MUSIC:
				interests.setMusic(true);
				break;
				
			case Constants.COOKING:
				interests.setCooking(true);
				break;
				
			case Constants.READING_BOOKS:
				interests.setReadingbooks(true);
				break;
				
			case Constants.DANCE:
				interests.setDance(true);
				break;
				
			case Constants.TECHNOLOGY:
				interests.setTechnology(true);
				break;
			
			default:
				System.out.println("Correct option is not present");
				return  null;
			}
		}
		interests.setCreatedat(Timestamp.valueOf("1970-01-01 00:00:01"));
		interests.setModifiedat(Timestamp.valueOf("1970-01-01 00:00:01"));
		return interests;
	}
}
