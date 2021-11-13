package com.convobee.data.repository;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.convobee.data.entity.Slots;

public interface SlotsRepo extends JpaRepository<Slots, Integer>{

	/*@Query(value ="SELECT s FROM Slots s WHERE slot_time LIKE :startDate%")
	LinkedList<Slots> findSlotsByDates(String startDate);*/
	
	@Query(value ="SELECT s.slotid FROM Slots s WHERE slot_time in :finalTime")
	LinkedList<Integer> findSlotsIdByDateTime(List<String> finalTime);
	
	@Query(value ="SELECT slot_id FROM slots  where slot_time between :startdatetime and :enddatetime",nativeQuery = true)
	Integer findSlotsIdByDateTimeRange(String startdatetime, String enddatetime);
	
	@Query(value ="select slots.slot_id from booked_slots inner join slots on booked_slots.slot_id=slots.slot_id and booked_slots.user_id=:userid and slots.slot_time>=:utc order by booked_slots.booked_slot_id DESC limit 5",nativeQuery = true)
	LinkedList<Integer> findSlotsIdByUTCTime(int userid, Timestamp utc);
	
	@Query(value ="SELECT slot_time FROM slots order by slot_id desc limit 1",nativeQuery = true)
	Timestamp findLastSlotTime();
}
 