package com.convobee.api.rest.request;

import java.util.HashMap;
import java.util.List;

import lombok.Data;

@Data
public class MeetingsRequest {

	private int bookedSlotId;
	private List<Integer> listOfUserIds;
	private HashMap<Integer, Integer> affectedUserIds;
	private int meetingId;
	private String status;
}
