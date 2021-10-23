package com.convobee.api.rest.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoCallResponse {

	List<MeetingResponse> meetingResponse;
	MeetingResponse.UnmatchedMeetingResponse unmatchedMeetingResponse;
}
