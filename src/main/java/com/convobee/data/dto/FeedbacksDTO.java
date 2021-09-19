package com.convobee.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FeedbacksDTO {

	private int feedback_id;
	private MeetingsDTO meetings;
	private UsersDTO provider_user_id;
	private UsersDTO receiver_user_id;
	private float impression_level;
	private float confidence_level;
	private float proficiency_level;
	private String appreciate_feedback;
	private String advise_feedback; 
	private Timestamp created_at;
}
