package com.convobee.service;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.convobee.api.rest.request.BookedSlotsRequest;
import com.convobee.data.entity.BookedSlots;
import com.convobee.data.mapper.BookedSlotsMapper;
import com.convobee.data.repository.BookedSlotsRepo;
import com.convobee.utils.UserUtil;

@Service
public class BookedSlotsService {

	@Autowired
	UserUtil userUtil;
	
	@Autowired
	BookedSlotsMapper bookedSlotsMapper;
	
	@Autowired
	BookedSlotsRepo bookedSlotsRepo;
	
	public Map<String, Integer> bookSlot(HttpServletRequest request, int slotid) {
		int userid = userUtil.getLoggedInUserId(request);
		BookedSlots bookedSlot = bookedSlotsMapper.mapBookedSlots(userid, slotid);
		BookedSlots bookedSlotAfterAddition = bookedSlotsRepo.save(bookedSlot);
		return getUpcomingSessions(request);//need to handle it properly
		//return bookedSlotAfterAddition.getBookedslotid();
	}
	
	public Map<String, Integer> rescheduleBookedSlot(HttpServletRequest request, BookedSlotsRequest bookedSlotsRequest) {
		BookedSlots rescheduleBookedSlot = bookedSlotsMapper.mapBookedSlotsForReschedule(bookedSlotsRequest);
		/* Need to be handled the user has to reschedule only his data not others, we can achieve this through transaction rollback after persisting also */
		BookedSlots bookedSlotAfterUpdation = bookedSlotsRepo.save(rescheduleBookedSlot);
		return getUpcomingSessions(request);
	}
	
	public Map<String, Integer> deleteBookedSlot(HttpServletRequest request, int bookedslotid) {
		BookedSlots deleteBookedSlot = bookedSlotsRepo.getById(bookedslotid);
		/* Handled the user has to delete only his data not others */
		if(deleteBookedSlot.getUsers().getUserid()==userUtil.getLoggedInUserId(request)) {
			bookedSlotsRepo.delete(deleteBookedSlot);
		}
		return getUpcomingSessions(request);
	}

	/* Need to handle the 3 minutes delay for showing upcoming session*/
	public Map<String, Integer> getUpcomingSessions(HttpServletRequest request) {
		int userid = userUtil.getLoggedInUserId(request);
		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		//System.out.println("DATETIME = " + utc);
		LinkedList<Object[]> listOfBookedslot = bookedSlotsRepo.findAllByUserid(userid, Timestamp.valueOf(utc.toLocalDateTime()));
		Map<String, Integer> finalMap = new LinkedHashMap<String, Integer>(); 
		int size = listOfBookedslot.size();
		for(int i = 0; i<size ;i++) {
			finalMap.put( String.valueOf(listOfBookedslot.get(i)[1].toString().replace('T', ' ')), Integer.valueOf(listOfBookedslot.get(i)[0].toString()));
		}
		//System.out.println("Final result = " + finalMap);
	    return finalMap;
	}
	
	
}
