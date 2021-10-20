package com.convobee.api.rest.request;

import lombok.Data;

@Data
public class MeetingsRequest {

	private int bookedSlotId;
	private int meetingId;
	private String status;
}
