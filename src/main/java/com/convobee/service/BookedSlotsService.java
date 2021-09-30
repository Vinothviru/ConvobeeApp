package com.convobee.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public String bookSlot(HttpServletRequest request, int slotid) {
		int userid = userUtil.getLoggedInUserId(request);
		BookedSlots bookedSlot = bookedSlotsMapper.mapBookedSlots(userid, slotid);
		bookedSlotsRepo.save(bookedSlot);
		return "OK";
	}
}
