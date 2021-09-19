package com.convobee.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FeedbacksToUsDTO {

	private int feedback_to_us_id;
	private MeetingsDTO meetings; 
	private UsersDTO provider_user_id; 
	private boolean report_user;
	private UsersDTO reportee_user_id; 
	private String report_type;
	private String report_description;
	private String feedback_to_us;
	private Timestamp created_at;
	
}
