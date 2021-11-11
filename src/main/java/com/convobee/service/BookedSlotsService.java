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
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.BookedSlotsRequest;
import com.convobee.api.rest.response.SessionResponse;
import com.convobee.constants.Constants;
import com.convobee.data.entity.BookedSlots;
import com.convobee.data.mapper.BookedSlotsMapper;
import com.convobee.data.repository.BookedSlotsRepo;
import com.convobee.exception.UserValidationException;
import com.convobee.utils.UserUtil;

@Transactional
@Service
public class BookedSlotsService {

	@Autowired
	UserUtil userUtil;
	
	@Autowired
	BookedSlotsMapper bookedSlotsMapper;
	
	@Autowired
	BookedSlotsRepo bookedSlotsRepo;
	
	@Autowired
	UsersService usersService;
	
	public LinkedList<SessionResponse> bookSlot(HttpServletRequest request, int slotid) {
		int userid = userUtil.getLoggedInUserId(request);
		BookedSlots bookedSlot = bookedSlotsMapper.mapBookedSlots(userid, slotid);
		bookedSlotsRepo.save(bookedSlot);
		return getUpcomingSessions(request);//need to handle it properly
		//return bookedSlotAfterAddition.getBookedslotid();
	}
	
	public LinkedList<SessionResponse> rescheduleBookedSlot(HttpServletRequest request, BookedSlotsRequest bookedSlotsRequest) throws Exception {
		BookedSlots rescheduleBookedSlot = bookedSlotsMapper.mapBookedSlotsForReschedule(request, bookedSlotsRequest);
		/* Handled the user has to reschedule only his data not others, we can achieve this through transaction rollback after persisting also */
		bookedSlotsRepo.save(rescheduleBookedSlot);
		return getUpcomingSessions(request);
	}
	
	public LinkedList<SessionResponse> deleteBookedSlot(HttpServletRequest request, int bookedslotid) throws Exception {
		BookedSlots deleteBookedSlot = bookedSlotsRepo.getById(bookedslotid);
		/* Handled the user has to delete only his data not others */
		if(!usersService.isValidUser(request, deleteBookedSlot.getUsers())) {
			throw new UserValidationException(Constants.USER_TRYING_TO_ACCESS_IRRELEVANT_DATA);
		}
		bookedSlotsRepo.delete(deleteBookedSlot);
		return getUpcomingSessions(request);
	}

	/* Need to handle the 3 minutes delay for showing upcoming session*/
	public LinkedList<SessionResponse> getUpcomingSessions(HttpServletRequest request) {
		int userid = userUtil.getLoggedInUserId(request);
		LinkedList<SessionResponse> sessionResponseList = new LinkedList<SessionResponse>(); 
		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		//System.out.println("DATETIME = " + utc);
		LinkedList<Object[]> listOfBookedslot = bookedSlotsRepo.findAllByUserid(userid, Timestamp.valueOf(utc.toLocalDateTime()));
		Map<String, Integer> finalMap = new LinkedHashMap<String, Integer>(); 
		int size = listOfBookedslot.size();
		for(int i = 0; i<size ;i++) {
			SessionResponse sessionResponse = new SessionResponse();
			sessionResponse.setBookedSlotId(Integer.valueOf(listOfBookedslot.get(i)[0].toString()));
			sessionResponse.setSlotTime(listOfBookedslot.get(i)[1].toString().replace('T', ' '));
			sessionResponseList.add(sessionResponse);
			//finalMap.put( String.valueOf(), );
		}
		//System.out.println("Final result = " + finalMap);
	    return sessionResponseList;
	}
	
	
}
