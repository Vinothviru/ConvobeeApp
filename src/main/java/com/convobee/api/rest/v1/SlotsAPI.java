package com.convobee.api.rest.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.SlotsRequest;
import com.convobee.service.SlotsService;

@RestController
public class SlotsAPI {
	
	@Autowired
	SlotsService slotsService;
	@RequestMapping(value = "/addslot", method = RequestMethod.POST)
	public ResponseEntity<?> addslot(@RequestBody SlotsRequest slotsRequest) throws Exception{
		slotsService.addSlot(slotsRequest);
		return ResponseEntity.ok(new String("Done"));
	}
}
