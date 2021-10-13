package com.convobee.api.rest.v1;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.InterestsRequest;
import com.convobee.data.entity.InterestsNames;
import com.convobee.data.repository.InterestNamesRepo;

@RestController
public class InterestsAdditionAPI {
	@Autowired
	InterestNamesRepo interestNamesRepo;
	@RequestMapping(value = "/addinterests", method = RequestMethod.POST)
	public String addInterests(@RequestBody InterestsRequest interestsRequest) throws Exception{
		List<InterestsNames> interestsNamesList = new LinkedList<InterestsNames>(); 
		for(String interestname : interestsRequest.getInterests()) {
			InterestsNames interest = new InterestsNames();
			interest.setInterestnames(interestname);
			interestsNamesList.add(interest);
		}
		
		interestNamesRepo.saveAll(interestsNamesList);
		return "Interests are added successfully";
	}
}
