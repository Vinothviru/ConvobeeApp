package com.convobee.data.mapper;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.UsersRequest;
import com.convobee.data.entity.Interests;
import com.convobee.data.entity.InterestsNames;
import com.convobee.data.entity.Users;
import com.convobee.utils.DateTimeUtil;

@Service
public class InterestsMapper {

	public List<Interests> mapInterestsFromRequest(Users user, UsersRequest usersRequest) throws Exception{
		List<String>  interestsList = usersRequest.getInterests();
		List<Interests> interestList = new LinkedList<Interests>();
		for(String interest : interestsList) {
			InterestsNames interestName = new InterestsNames();
			interestName.setInterestnames(interest);
			Interests interests = new Interests();
			interests.setUser(user);
			interests.setInterest(interestName);
			interests.setCreatedat(DateTimeUtil.getCurrentUTCTime());
			interests.setModifiedat(DateTimeUtil.getCurrentUTCTime());
			interestList.add(interests);
		}
		return interestList;
	}
}
