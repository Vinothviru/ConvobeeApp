package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.MeetingsRequest;
import com.convobee.api.rest.request.UsersRequest;
import com.convobee.api.rest.response.VideoCallResponse;
import com.convobee.exception.UserValidationException;
import com.convobee.service.MeetingsService;

@RestController
public class VideoCallAPI {

	@Autowired
	MeetingsService meetingsService;
	
	@RequestMapping(value = "/initiatemeeting", method = RequestMethod.POST)
	public VideoCallResponse initiateMeeting(HttpServletRequest request, @RequestBody MeetingsRequest meetingsRequest)
	{
		return meetingsService.initiateMeeting(meetingsRequest);
	}

	@RequestMapping(value = "/initiatemeetingforsecondcall", method = RequestMethod.POST)
	public VideoCallResponse initiateMeetingForSecondCall(HttpServletRequest request, @RequestBody MeetingsRequest meetingsRequest)
	{
		return meetingsService.initiateMeetingForSecondCall(meetingsRequest);
	}
	
	@RequestMapping(value = "/changestatusofmeetingtostarted", method = RequestMethod.PUT)
	public String changeStatusOfMeetingtoStarted(HttpServletRequest request, @ModelAttribute MeetingsRequest meetingsRequest) throws UserValidationException
	{
		meetingsService.changeStatusOfMeetingtoStarted(request, meetingsRequest);
		return "OK";
	}
	
	@RequestMapping(value = "/changestatusofmeetingtocompleted", method = RequestMethod.PUT)
	public String changeStatusOfMeetingtoCompleted(HttpServletRequest request, @ModelAttribute MeetingsRequest meetingsRequest) throws UserValidationException
	{
		meetingsService.changeStatusOfMeetingtoCompleted(request, meetingsRequest);
		return "OK";
	}
	
	@RequestMapping(value = "/prevalidationofjoinsession", method = RequestMethod.GET)
	public String prevalidationOfJoinSession(HttpServletRequest request, @ModelAttribute UsersRequest usersRequest) throws Exception
	{
		meetingsService.prevalidationOfJoinSession(request, usersRequest);
		return "OK";
	}
}
