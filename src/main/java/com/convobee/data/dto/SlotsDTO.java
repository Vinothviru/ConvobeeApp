package com.convobee.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class SlotsDTO {

	private int slot_id;
	private Timestamp slot_time;
	private String slot_url;
	private Timestamp created_at;
	
}
