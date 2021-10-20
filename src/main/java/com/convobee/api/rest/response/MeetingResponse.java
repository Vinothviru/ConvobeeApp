package com.convobee.api.rest.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingResponse {
	private int meetingId;
	private int currentUserId;
	private int oppositeUserId;
	private String meetingUrl;
	private List<String> oppositeUserMatchedInterest;
	private List<String> oppositeUserInterests;
	
	@Data
	public static class UnmatchedMeetingResponse {
		private Integer unMatchedUserId;
	}
}
