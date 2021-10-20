package com.convobee.api.rest.response.builder;

import java.util.List;

import org.springframework.stereotype.Service;

import com.convobee.api.rest.response.MeetingResponse;
import com.convobee.api.rest.response.VideoCallResponse;

@Service
public class VideoCallResponseBuilder {
	public VideoCallResponse buildResponse(List<MeetingResponse> meetingResponse, MeetingResponse.UnmatchedMeetingResponse unmatchedMeetingResponse) {
		VideoCallResponse videoCallResponse = VideoCallResponse.builder()
																	.meetingResponse(meetingResponse)
																	.unmatchedMeetingResponse(unmatchedMeetingResponse).build();
		return videoCallResponse;
	}
	
}
