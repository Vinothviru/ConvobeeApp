package com.convobee.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MeetingsDTO {

	private int meeting_id;
	private SlotsDTO slots;
	private UsersDTO user_a_id;
	private UsersDTO user_b_id;
	private String meeting_url;
	private boolean meeting_status;
	private Timestamp started_at;
	private Timestamp ended_at;
	
}
