package com.convobee.data.mapper;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.BookedSlotsRequest;
import com.convobee.data.entity.BookedSlots;
import com.convobee.data.entity.Slots;
import com.convobee.data.entity.Users;
import com.convobee.data.repository.BookedSlotsRepo;

@Service
public class BookedSlotsMapper {

	@Autowired
	BookedSlotsRepo bookedSlotsRepo;
	
	public BookedSlots mapBookedSlots(int userid, int slotid) {
		BookedSlots bookedSlots = new BookedSlots();	
		Users user = new Users();
		user.setUserid(userid);
		Slots slot = new Slots();
		slot.setSlotid(slotid);
		bookedSlots.setUsers(user);
		bookedSlots.setSlots(slot);
		bookedSlots.setCreatedat(Timestamp.valueOf("1970-01-01 00:00:01"));
		bookedSlots.setModifiedat(Timestamp.valueOf("1970-01-01 00:00:01"));
		return bookedSlots;
	}
	
	@Transactional
	public BookedSlots mapBookedSlotsForReschedule(BookedSlotsRequest bookedSlotsRequest) {
		BookedSlots rescheduleSlot = bookedSlotsRepo.getById(bookedSlotsRequest.getBookedslotid());
		int newSlotId = bookedSlotsRequest.getNewslotid();
		Slots slot = new Slots();
		slot.setSlotid(newSlotId);
		rescheduleSlot.setSlots(slot);
		return rescheduleSlot;
	}
}
