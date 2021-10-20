package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.MeetingsRequest;
import com.convobee.api.rest.response.VideoCallResponse;
import com.convobee.service.MeetingsService;

@RestController
public class VideoCallAPI {

	@Autowired
	MeetingsService meetingsService;
	@RequestMapping(value = "/initiatemeeting", method = RequestMethod.POST)
	public VideoCallResponse initiateMeeting(HttpServletRequest request, @ModelAttribute MeetingsRequest meetingsRequest)
	{
		return meetingsService.addActiveUsers(request, meetingsRequest);
	}
}
