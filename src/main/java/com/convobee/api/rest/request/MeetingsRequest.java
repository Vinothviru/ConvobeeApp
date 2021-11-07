package com.convobee.api.rest.request;

import java.util.List;

import lombok.Data;

@Data
public class MeetingsRequest {

	@Deprecated
	private int bookedSlotId;
	private List<Integer> listOfUserIds;
	private List<Integer> affectedMeetingIds;
	private int meetingId;
}
