package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.MeetingsRequest;
import com.convobee.api.rest.request.UsersRequest;
import com.convobee.api.rest.response.BaseResponse;
import com.convobee.exception.UserValidationException;
import com.convobee.service.MeetingsService;

@RestController
@CrossOrigin
public class VideoCallAPI {

	@Autowired
	MeetingsService meetingsService;
	
	@RequestMapping(value = "/initiatemeeting", method = RequestMethod.POST)
	public ResponseEntity initiateMeeting(HttpServletRequest request, @RequestBody MeetingsRequest meetingsRequest)
	{
		return  new BaseResponse().getResponse( ()-> meetingsService.initiateMeeting(meetingsRequest));
	}

	@RequestMapping(value = "/initiatemeetingforsecondcall", method = RequestMethod.POST)
	public ResponseEntity initiateMeetingForSecondCall(HttpServletRequest request, @RequestBody MeetingsRequest meetingsRequest)
	{
		return  new BaseResponse().getResponse( ()-> meetingsService.initiateMeetingForSecondCall(meetingsRequest));
	}
	
	@RequestMapping(value = "/changestatusofmeetingtostarted", method = RequestMethod.PUT)
	public ResponseEntity changeStatusOfMeetingtoStarted(HttpServletRequest request, @ModelAttribute MeetingsRequest meetingsRequest) throws UserValidationException
	{
		return  new BaseResponse().getResponse( ()-> meetingsService.changeStatusOfMeetingtoStarted(request, meetingsRequest));
	}
	
	@RequestMapping(value = "/changestatusofmeetingtocompleted", method = RequestMethod.PUT)
	public ResponseEntity changeStatusOfMeetingtoCompleted(HttpServletRequest request, @ModelAttribute MeetingsRequest meetingsRequest) throws UserValidationException
	{
		return  new BaseResponse().getResponse( ()-> meetingsService.changeStatusOfMeetingtoCompleted(request, meetingsRequest));
	}
	
	@RequestMapping(value = "/prevalidationofjoinsession", method = RequestMethod.GET)
	public ResponseEntity prevalidationOfJoinSession(HttpServletRequest request, @ModelAttribute UsersRequest usersRequest) throws Exception
	{
		return  new BaseResponse().getResponse( ()-> meetingsService.prevalidationOfJoinSession(request, usersRequest));
	}
}
