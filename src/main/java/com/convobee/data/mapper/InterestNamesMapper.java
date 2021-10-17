package com.convobee.data.mapper;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.InterestsRequest;
import com.convobee.data.entity.InterestsNames;

@Service
public class InterestNamesMapper {

	public List<InterestsNames> mapInterestNamesFromRequest(InterestsRequest interestsRequest) throws Exception{
		List<InterestsNames> interestsNamesList = new LinkedList<InterestsNames>(); 
		for(String interestname : interestsRequest.getInterests()) {
			InterestsNames interest = new InterestsNames();
			interest.setInterestnames(interestname);
			interestsNamesList.add(interest);
		}
		return interestsNamesList;
	}
}
