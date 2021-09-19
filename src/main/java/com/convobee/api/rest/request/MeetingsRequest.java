package com.convobee.api.rest.request;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MeetingsRequest {

	private int meeting_id;
	private SlotsRequest slots;
	private UsersRequest user_a_id;
	private UsersRequest user_b_id;
	private String meeting_url;
	private boolean meeting_status;
	private Timestamp started_at;
	private Timestamp ended_at;
	
}
