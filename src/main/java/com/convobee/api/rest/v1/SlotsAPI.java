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
	public int bookSlot(HttpServletRequest request, @PathVariable int slotid)
	{
		int bookedSLotId = bookedSlotsService.bookSlot(request, slotid);
		return bookedSLotId;
	}
	
	@RequestMapping(value = "/rescheduleslot", method = RequestMethod.PUT)
	public int rescheduleBookedSlot(@RequestBody BookedSlotsRequest bookedSlotsRequest) 
	{
		int bookedSlotId = bookedSlotsService.rescheduleBookedSlot(bookedSlotsRequest);
		return bookedSlotId;
	}
	
	@RequestMapping(value = "/deleteslot/{bookedslotid}", method = RequestMethod.DELETE)
	public String deleteBookedSlot(@PathVariable int bookedslotid) 
	{
		String isDeletedSlot = bookedSlotsService.deleteBookedSlot(bookedslotid);
		return isDeletedSlot;
	}
}
