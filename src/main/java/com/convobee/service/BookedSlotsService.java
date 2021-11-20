package com.convobee.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.BookedSlotsRequest;
import com.convobee.api.rest.request.SlotsRequest;
import com.convobee.api.rest.response.SessionResponse;
import com.convobee.constants.Constants;
import com.convobee.data.entity.BookedSlots;
import com.convobee.data.mapper.BookedSlotsMapper;
import com.convobee.data.repository.BookedSlotsRepo;
import com.convobee.exception.UserValidationException;
import com.convobee.utils.DateTimeUtil;
import com.convobee.utils.UserUtil;

@Transactional(rollbackFor = Exception.class)
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
	
	public LinkedList<SessionResponse> bookSlot(HttpServletRequest request, SlotsRequest slotsRequest) {
		int userid = userUtil.getLoggedInUserId(request);
		int slotid = slotsRequest.getSlotId();
		BookedSlots bookedSlot = bookedSlotsMapper.mapBookedSlots(userid, slotid);
		bookedSlotsRepo.save(bookedSlot);
		return getUpcomingSessions(request, slotsRequest.getTimeZone());//need to handle it properly
		//return bookedSlotAfterAddition.getBookedslotid();
	}
	
	public LinkedList<SessionResponse> rescheduleBookedSlot(HttpServletRequest request, BookedSlotsRequest bookedSlotsRequest) throws Exception {
		BookedSlots rescheduleBookedSlot = bookedSlotsMapper.mapBookedSlotsForReschedule(request, bookedSlotsRequest);
		/* Handled the user has to reschedule only his data not others, we can achieve this through transaction rollback after persisting also */
		bookedSlotsRepo.save(rescheduleBookedSlot);
		return getUpcomingSessions(request, bookedSlotsRequest.getTimeZone());
	}
	
	public LinkedList<SessionResponse> deleteBookedSlot(HttpServletRequest request, BookedSlotsRequest bookedSlotsRequest) throws Exception {
		BookedSlots deleteBookedSlot = bookedSlotsRepo.getById(bookedSlotsRequest.getBookedslotid());
		/* Handled the user has to delete only his data not others */
		if(!usersService.isValidUser(request, deleteBookedSlot.getUsers())) {
			throw new UserValidationException(Constants.USER_TRYING_TO_ACCESS_IRRELEVANT_DATA);
		}
		bookedSlotsRepo.delete(deleteBookedSlot);
		return getUpcomingSessions(request, bookedSlotsRequest.getTimeZone());
	}

	/* Handled the 2 minutes delay for showing upcoming session, purpose is to make the user to join session if they loggedin 2 minutes late also */
	public LinkedList<SessionResponse> getUpcomingSessions(HttpServletRequest request, String timeZone) {
		int userid = userUtil.getLoggedInUserId(request);
		LinkedList<SessionResponse> sessionResponseList = new LinkedList<SessionResponse>(); 
		ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		utc = utc.minusMinutes(3);//3 means, at backend query will check for extra 2 minutes only
		LinkedList<Object[]> listOfBookedslot = bookedSlotsRepo.findAllByUserid(userid, Timestamp.valueOf(utc.toLocalDateTime()));
		int size = listOfBookedslot.size();
		for(int i = 0; i<size ;i++) {
			SessionResponse sessionResponse = new SessionResponse();
			sessionResponse.setBookedSlotId(Integer.valueOf(listOfBookedslot.get(i)[0].toString()));
			LocalDateTime ldt = LocalDateTime.parse(listOfBookedslot.get(i)[1].toString().replace(' ', 'T'));
		    ldt = DateTimeUtil.toZone(ldt, ZoneId.of(timeZone));
			String month = Character.toUpperCase(ldt.getMonth().toString().toLowerCase().charAt(0))+ldt.getMonth().toString().substring(1,3).toLowerCase();
			String date = String.valueOf(ldt.getDayOfMonth());
			String day = Character.toUpperCase(String.valueOf(ldt.getDayOfWeek()).charAt(0))+ String.valueOf(ldt.getDayOfWeek()).substring(1, 3).toLowerCase();
			String finalDateTimeValue = day+", "+month+" "+date+" "+ldt.getYear()+" "+ldt.getHour()+":"+ldt.getMinute();
			sessionResponse.setSlotTime(finalDateTimeValue);
			sessionResponseList.add(sessionResponse);
		}
	    return sessionResponseList;
	}
	
	
}
