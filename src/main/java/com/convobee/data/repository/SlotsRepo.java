package com.convobee.data.repository;

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
}
 