package com.convobee.api.rest.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.InterestsRequest;
import com.convobee.api.rest.response.BaseResponse;
import com.convobee.service.InterestsService;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
public class InterestsAdditionAPI {
	@Autowired
	InterestsService interestsService;
	
	/* Added only by admin */
	@RequestMapping(value = "/addinterests", method = RequestMethod.POST)
	public ResponseEntity addInterests(@RequestBody InterestsRequest interestsRequest) throws Exception{
		return  new BaseResponse().getResponse( ()-> interestsService.addInterests(interestsRequest));
	}
}
