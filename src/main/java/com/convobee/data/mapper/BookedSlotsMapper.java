package com.convobee.data.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.convobee.api.rest.request.BookedSlotsRequest;
import com.convobee.data.entity.BookedSlots;
import com.convobee.data.entity.Slots;
import com.convobee.data.entity.Users;
import com.convobee.data.repository.BookedSlotsRepo;
import com.convobee.utils.DateTimeUtil;

@Transactional
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
		bookedSlots.setCreatedat(DateTimeUtil.getCurrentUTCTime());
		bookedSlots.setModifiedat(DateTimeUtil.getCurrentUTCTime());
		return bookedSlots;
	}
	
	public BookedSlots mapBookedSlotsForReschedule(BookedSlotsRequest bookedSlotsRequest) {
		BookedSlots rescheduleSlot = bookedSlotsRepo.getById(bookedSlotsRequest.getBookedslotid());
		int newSlotId = bookedSlotsRequest.getNewslotid();
		Slots slot = new Slots();
		slot.setSlotid(newSlotId);
		rescheduleSlot.setSlots(slot);
		return rescheduleSlot;
	}
}
