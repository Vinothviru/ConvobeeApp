package com.convobee.api.rest.request;

import lombok.Data;

@Data
public class FeedbacksHistoryRequest {
	int feedbackId;
	String timeZone;
	String startDate;
	String endDate;
}
