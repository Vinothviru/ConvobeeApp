package com.convobee.api.rest.request;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FeedbacksToUsRequest {

	private int feedback_to_us_id;
	private MeetingsRequest meetings; 
	private UsersRequest provider_user_id; 
	private boolean report_user;
	private UsersRequest reportee_user_id; 
	private String report_type;
	private String report_description;
	private String feedback_to_us;
	private Timestamp created_at;
	
}
