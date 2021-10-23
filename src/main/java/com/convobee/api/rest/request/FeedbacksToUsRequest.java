package com.convobee.api.rest.request;

import lombok.Data;

@Data
public class FeedbacksToUsRequest {
	private int meetingId; 
	private boolean isreport_user;
	//private boolean isfeedback_given;
	private String reportType;
	private String reportDescription;
	private String feedbackToProduct;
}
