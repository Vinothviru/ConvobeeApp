package com.convobee.data.dto;

import java.sql.Timestamp;

import lombok.Data;
@Data
public class BookedSlotsDTO {

	private int booked_slot_id;
	private UsersDTO users;
	private SlotsDTO slots;
	private Timestamp created_at;
	private Timestamp modified_at;
	
}
