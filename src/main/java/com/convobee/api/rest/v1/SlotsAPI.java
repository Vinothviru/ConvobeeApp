package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.BookedSlotsRequest;
import com.convobee.api.rest.request.SlotsRequest;
import com.convobee.api.rest.response.BaseResponse;
import com.convobee.service.BookedSlotsService;
import com.convobee.service.SlotsService;

@RestController
public class SlotsAPI {
	
	@Autowired
	SlotsService slotsService;
	
	@Autowired
	BookedSlotsService bookedSlotsService;
	
	/* Added only by admin */
	@RequestMapping(value = "/addslot", method = RequestMethod.POST)
	public ResponseEntity addSlot(@RequestBody SlotsRequest slotsRequest) throws Exception{
		return  new BaseResponse().getResponse( ()-> slotsService.addSlot(slotsRequest));
	}
	
	@RequestMapping(value = "/showslots", method = RequestMethod.GET)
	public ResponseEntity showSlots(HttpServletRequest request, @ModelAttribute SlotsRequest slotsRequest) throws Exception{
		return  new BaseResponse().getResponse( ()-> slotsService.showSlots(request, slotsRequest.getTimezone()));
	}
	
	@RequestMapping(value = "/bookslot/{slotid}", method = RequestMethod.POST)
	public ResponseEntity bookSlot(HttpServletRequest request, @PathVariable int slotid)
	{
		return  new BaseResponse().getResponse( ()-> bookedSlotsService.bookSlot(request, slotid));
	}
	
	@RequestMapping(value = "/rescheduleslot", method = RequestMethod.PUT)
	public ResponseEntity rescheduleBookedSlot(HttpServletRequest request, @RequestBody BookedSlotsRequest bookedSlotsRequest) throws Exception 
	{
		return  new BaseResponse().getResponse( ()-> bookedSlotsService.rescheduleBookedSlot(request,bookedSlotsRequest));
	}
	
	@RequestMapping(value = "/deleteslot/{bookedslotid}", method = RequestMethod.DELETE)
	public ResponseEntity deleteBookedSlot(HttpServletRequest request, @PathVariable int bookedslotid) throws Exception 
	{
		return  new BaseResponse().getResponse( ()-> bookedSlotsService.deleteBookedSlot(request, bookedslotid));
	}
	
	@RequestMapping(value = "/getupcomingsessions", method = RequestMethod.GET)
	public ResponseEntity getUpcomingSessions(HttpServletRequest request) 
	{
		return  new BaseResponse().getResponse( ()-> bookedSlotsService.getUpcomingSessions(request));
		//System.out.println("Final result at API = " + slotList);
	}
}
