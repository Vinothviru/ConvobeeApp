package com.convobee.service;

import java.sql.Timestamp;
import java.util.HashMap;
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

	public Map<Timestamp, Integer> getUpcomingSessions(HttpServletRequest request) {
		int userid = userUtil.getLoggedInUserId(request);
		/*Users user = new Users();
		user.setUserid(userid);*/
		//System.out.println(bookedSlotsRepo.findAllByUserid(userid));
		LinkedList<Object[]> listOfBookedslot = bookedSlotsRepo.findAllByUserid(userid);
		//Collection<BookedSlotsResponse> listOfBookedslot = bookedSlotsRepo.findAllByUserid(userid);
		Map<Timestamp, Integer> finalMap = new LinkedHashMap<Timestamp, Integer>(); 
		/*for(Map<Integer, Timestamp> map : listOfBookedslot)
		{
			System.out.println(map.keySet() + " " + map.values());
			finalMap.put(map.get(0), map.get(1));
		}*/
		//System.out.println(listOfBookedslot.get(0)[0] + " " + listOfBookedslot.get(0)[1]);
		//System.out.println(listOfBookedslot.get(1)[0] + " " + listOfBookedslot.get(1)[1]);
		int size = listOfBookedslot.size();
		for(int i = 0; i<size ;i++) {
			finalMap.put( Timestamp.valueOf(listOfBookedslot.get(i)[1].toString()), Integer.valueOf(listOfBookedslot.get(i)[0].toString()));
		}
		//finalMap.put(Integer.valueOf(listOfBookedslot.get(0)[0].toString()), Timestamp.valueOf(listOfBookedslot.get(0)[1].toString()));
		System.out.println("Final result = " + finalMap);
		//System.out.println(listOfBookedslot);
		//System.out.println(listOfBookedslot.get(0)[0]);
		//System.out.println(listOfBookedslot);
	    //List<BookedSlots> bookedSlotList= bookedSlotsRepo.findAllByUsers(user);
	    /*Map<Integer, Timestamp> returnSlots = new TreeMap<Integer, Timestamp>();
	    for(BookedSlots bookedSlot : bookedSlotList)
	    {
	    	Slots slot = bookedSlot.getSlots();
	    	returnSlots.put(bookedSlot.getBookedslotid(), slot.getSlottime());
	    }
	    System.out.println(returnSlots);*/
	    return finalMap;
	}
	
	
}
