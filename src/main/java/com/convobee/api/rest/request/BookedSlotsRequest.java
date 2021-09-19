package com.convobee.api.rest.request;

import java.sql.Timestamp;

import lombok.Data;
@Data
public class BookedSlotsRequest {

	private int booked_slot_id;
	private UsersRequest users;
	private SlotsRequest slots;
	private Timestamp created_at;
	private Timestamp modified_at;
	
}
