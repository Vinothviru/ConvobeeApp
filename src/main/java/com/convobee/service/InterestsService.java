package com.convobee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.InterestsRequest;
import com.convobee.data.entity.InterestsNames;
import com.convobee.data.mapper.InterestNamesMapper;
import com.convobee.data.repository.InterestNamesRepo;

@Transactional(rollbackFor = Exception.class)
@Service
public class InterestsService {
	@Autowired
	InterestNamesRepo interestNamesRepo;
	
	@Autowired
	InterestNamesMapper interestNamesMapper;
	
	public boolean addInterests(InterestsRequest interestsRequest) throws Exception{
		List<InterestsNames> interestsNamesList = interestNamesMapper.mapInterestNamesFromRequest(interestsRequest);
		interestNamesRepo.saveAll(interestsNamesList);
		return true;
	}
}
