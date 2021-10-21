package com.convobee.api.rest.request;

import lombok.Data;

@Data
public class FeedbacksRequest {

	private int meetingId;
	private float impressionLevel;
	private float confidenceLevel;
	private float proficiencyLevel;
	private String appreciateFeedback;
	private String adviseFeedback; 
}
