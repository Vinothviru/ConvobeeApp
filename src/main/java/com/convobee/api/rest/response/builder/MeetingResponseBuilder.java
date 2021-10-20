package com.convobee.api.rest.response.builder;

import java.util.List;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.MeetingResponse;

@Service
public class MeetingResponseBuilder {
	public MeetingResponse buildResponse(int meetingId, int currentUserId, int oppositeUserId, 
											String meetingUrl, List<String> oppositeUserMatchedInterest,
											List<String> oppositeUserUnmatchedInterest){
		MeetingResponse meetingResponse = MeetingResponse.builder()
														.meetingId(meetingId)
														.currentUserId(currentUserId)
														.oppositeUserId(oppositeUserId)
														.meetingUrl(meetingUrl)
														.oppositeUserMatchedInterest(oppositeUserMatchedInterest)
														.oppositeUserUnmatchedInterest(oppositeUserUnmatchedInterest).build();
		return meetingResponse;
	}
}
