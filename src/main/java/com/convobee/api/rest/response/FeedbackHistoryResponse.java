package com.convobee.api.rest.response;

import lombok.Data;

@Data
public class FeedbackHistoryResponse {
	private String slotDateTime;
	private String peerNickName;
	private int feedbackId;
}
