package com.convobee.api.rest.request;

import java.util.List;

import lombok.Data;

@Data
public class MeetingsRequest {

	private int bookedSlotId;
	private List<Integer> listOfUserIds;
	private int meetingId;
	private String status;
}
