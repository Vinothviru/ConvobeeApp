package com.convobee.data.repository;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.convobee.data.entity.BookedSlots;

public interface BookedSlotsRepo extends JpaRepository<BookedSlots, Integer>{
	
	@Query(value = "select booked_slot_id, slot_time from (select booked_slots.booked_slot_id, slots.slot_time from booked_slots inner join slots on booked_slots.slot_id=slots.slot_id and booked_slots.user_id=:userid and slots.slot_time>:utc order by booked_slots.booked_slot_id DESC) as t order by slot_time ASC",nativeQuery = true)
	LinkedList<Object[]> findBookedSlotIdAndSlotTimeByUserid(int userid, Timestamp utc);
	
	@Query(value = "SELECT meeting_id FROM meetings where (user_a_id=:userid or user_b_id=:userid) and started_at>:utc and meeting_status like '%Initiated%'",nativeQuery = true)
	Integer findMeetingIdByStatusOfUser(int userid, Timestamp utc);
	
	@Query(value ="select slot_id from booked_slots where booked_slot_id=:bookedslotid",nativeQuery = true)
	Integer findSlotIdByBookedslotid(int bookedslotid);
	
	@Query(value = "SELECT slot_id,count(*) FROM booked_slots where slot_id in :finalSlotIds group by slot_id",nativeQuery = true)
	LinkedList<Object[]> findCountOfSlotsBySlotids(List<Integer> finalSlotIds);
}
