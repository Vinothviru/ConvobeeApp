package com.convobee.data.mapper;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.convobee.data.entity.BookedSlots;
import com.convobee.data.entity.Slots;
import com.convobee.data.entity.Users;

@Service
public class BookedSlotsMapper {

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
}
