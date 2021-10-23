package com.convobee.api.rest.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.InterestsRequest;
import com.convobee.service.InterestsService;

@RestController
public class InterestsAdditionAPI {
	@Autowired
	InterestsService interestsService;
	
	@RequestMapping(value = "/addinterests", method = RequestMethod.POST)
	public String addInterests(@RequestBody InterestsRequest interestsRequest) throws Exception{
		interestsService.addInterests(interestsRequest);
		return "Interests are added successfully";
	}
}
