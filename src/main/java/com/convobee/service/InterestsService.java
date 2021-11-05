package com.convobee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.InterestsRequest;
import com.convobee.data.entity.InterestsNames;
import com.convobee.data.mapper.InterestNamesMapper;
import com.convobee.data.repository.InterestNamesRepo;

@Transactional
@Service
public class InterestsService {
	@Autowired
	InterestNamesRepo interestNamesRepo;
	
	@Autowired
	InterestNamesMapper interestNamesMapper;
	
	public void addInterests(InterestsRequest interestsRequest) throws Exception{
		List<InterestsNames> interestsNamesList = interestNamesMapper.mapInterestNamesFromRequest(interestsRequest);
		interestNamesRepo.saveAll(interestsNamesList);
	}
}
