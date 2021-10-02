package com.convobee.service;

import java.sql.Timestamp;
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
	
	public int bookSlot(HttpServletRequest request, int slotid) {
		int userid = userUtil.getLoggedInUserId(request);
		BookedSlots bookedSlot = bookedSlotsMapper.mapBookedSlots(userid, slotid);
		BookedSlots bookedSlotAfterAddition = bookedSlotsRepo.save(bookedSlot);
		getUpcomingSessions(request);//need to handle it properly
		return bookedSlotAfterAddition.getBookedslotid();
	}
	
	public int rescheduleBookedSlot(HttpServletRequest request, BookedSlotsRequest bookedSlotsRequest) {
		BookedSlots rescheduleBookedSlot = bookedSlotsMapper.mapBookedSlotsForReschedule(bookedSlotsRequest);
		BookedSlots bookedSlotAfterUpdation = bookedSlotsRepo.save(rescheduleBookedSlot);
		getUpcomingSessions(request);
		return bookedSlotAfterUpdation.getBookedslotid();
	}
	
	public String deleteBookedSlot(HttpServletRequest request, int bookedslotid) {
		BookedSlots deleteBookedSlot = bookedSlotsRepo.getById(bookedslotid);
		bookedSlotsRepo.delete(deleteBookedSlot);
		getUpcomingSessions(request);
		return "Successfully deleted";
	}

	public String getUpcomingSessions(HttpServletRequest request) {
		int userid = userUtil.getLoggedInUserId(request);
		/*Users user = new Users();
		user.setUserid(userid);*/
		//System.out.println(bookedSlotsRepo.findAllByUserid(userid));
		LinkedList<Map<Integer, Timestamp>> listOfBookedslot = bookedSlotsRepo.findAllByUserid(userid);
		for(Map<Integer, Timestamp> map : listOfBookedslot)
		{
			System.out.println(map.keySet() + " " + map.values());
		}
	    //List<BookedSlots> bookedSlotList= bookedSlotsRepo.findAllByUsers(user);
	    /*Map<Integer, Timestamp> returnSlots = new TreeMap<Integer, Timestamp>();
	    for(BookedSlots bookedSlot : bookedSlotList)
	    {
	    	Slots slot = bookedSlot.getSlots();
	    	returnSlots.put(bookedSlot.getBookedslotid(), slot.getSlottime());
	    }
	    System.out.println(returnSlots);*/
	    return "OK";
	}
	
	
}
