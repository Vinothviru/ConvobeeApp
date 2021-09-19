package com.convobee.api.rest.request;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class SlotsRequest {

	private int slot_id;
	private Timestamp slot_time;
	private String slot_url;
	private Timestamp created_at;
	
}
