package com.convobee.data.repository;

import java.sql.Timestamp;
import java.util.LinkedList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.convobee.data.entity.BookedSlots;

public interface BookedSlotsRepo extends JpaRepository<BookedSlots, Integer>{
	
	@Query(value = "select booked_slot_id, slot_time from (select booked_slots.booked_slot_id, slots.slot_time from booked_slots inner join slots on booked_slots.slot_id=slots.slot_id and booked_slots.user_id=:userid and slots.slot_time>:utc order by booked_slots.booked_slot_id DESC limit 5) as t order by slot_time ASC",nativeQuery = true)
	LinkedList<Object[]> findAllByUserid(int userid, Timestamp utc);
	
	BookedSlots findSlotsByBookedslotid(int bookedslotid);
	
}
