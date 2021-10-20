package com.convobee.api.rest.v1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.response.MeetingResponse;
import com.convobee.service.MeetingsService;

@RestController
public class VideoCallAPI {

	@Autowired
	MeetingsService meetingsService;
	@RequestMapping(value = "/initiatemeeting", method = RequestMethod.POST)
	public List<MeetingResponse> initiateMeeting(HttpServletRequest request)
	{
		return meetingsService.addActiveUsers(request);
	}
}
