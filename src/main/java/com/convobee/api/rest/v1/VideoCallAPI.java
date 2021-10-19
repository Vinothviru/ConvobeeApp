package com.convobee.api.rest.v1;

import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.service.MeetingsService;

@RestController
public class VideoCallAPI {

	@Autowired
	MeetingsService meetingsService;
	@RequestMapping(value = "/initiatemeeting", method = RequestMethod.POST)
	public Map<LinkedList<Integer>, String> initiateMeeting(HttpServletRequest request)
	{
		return meetingsService.addActiveUsers(request);
	}
}
