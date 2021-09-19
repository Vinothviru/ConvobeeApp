package com.convobee.api.rest.request;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FeedbacksRequest {

	private int feedback_id;
	private MeetingsRequest meetings;
	private UsersRequest provider_user_id;
	private UsersRequest receiver_user_id;
	private float impression_level;
	private float confidence_level;
	private float proficiency_level;
	private String appreciate_feedback;
	private String advise_feedback; 
	private Timestamp created_at;
}
