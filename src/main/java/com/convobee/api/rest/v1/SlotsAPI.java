package com.convobee.api.rest.v1;

import java.util.Map;

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
import com.convobee.service.BookedSlotsService;
import com.convobee.service.SlotsService;

@RestController
public class SlotsAPI {
	
	@Autowired
	SlotsService slotsService;
	
	@Autowired
	BookedSlotsService bookedSlotsService;
	
	@RequestMapping(value = "/addslot", method = RequestMethod.POST)
	public ResponseEntity<?> addSlot(@RequestBody SlotsRequest slotsRequest) throws Exception{
		slotsService.addSlot(slotsRequest);
		return ResponseEntity.ok(new String("Done"));
	}
	
	@RequestMapping(value = "/showslots", method = RequestMethod.GET)
	public Map<String, Map<Integer, String>> showSlots(@ModelAttribute SlotsRequest slotsRequest) throws Exception{
		return slotsService.showSlots(slotsRequest.getTimezone());
		
		//return ResponseEntity.ok(new String("Done"));
	}
	
	@RequestMapping(value = "/bookslot/{slotid}", method = RequestMethod.POST)
	public Map<String, Integer> bookSlot(HttpServletRequest request, @PathVariable int slotid)
	{
		Map<String, Integer> bookedSLotId = bookedSlotsService.bookSlot(request, slotid);
		return bookedSLotId;
	}
	
	@RequestMapping(value = "/rescheduleslot", method = RequestMethod.PUT)
	public Map<String, Integer> rescheduleBookedSlot(HttpServletRequest request, @RequestBody BookedSlotsRequest bookedSlotsRequest) 
	{
		Map<String, Integer> bookedSlotId = bookedSlotsService.rescheduleBookedSlot(request,bookedSlotsRequest);
		return bookedSlotId;
	}
	
	@RequestMapping(value = "/deleteslot/{bookedslotid}", method = RequestMethod.DELETE)
	public Map<String, Integer> deleteBookedSlot(HttpServletRequest request, @PathVariable int bookedslotid) 
	{
		Map<String, Integer> isDeletedSlot = bookedSlotsService.deleteBookedSlot(request, bookedslotid);
		return isDeletedSlot;
	}
	
	@RequestMapping(value = "/getupcomingsessions", method = RequestMethod.GET)
	public Map<String, Integer> getUpcomingSessions(HttpServletRequest request) 
	{
		Map<String, Integer> slotList = bookedSlotsService.getUpcomingSessions(request);
		System.out.println("Final result at API = " + slotList);
		return slotList;
	}
}
