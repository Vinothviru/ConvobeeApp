package com.convobee.api.rest.v1;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.convobee.api.rest.request.FeedbacksRequest;
import com.convobee.service.FeedbacksService;

@RestController
public class FeedbackAPI {

	@Autowired
	FeedbacksService feedbacksService;

	@RequestMapping(value = "/submitfeedback", method = RequestMethod.POST)
	public String submitFeedback(HttpServletRequest request, @RequestBody FeedbacksRequest feedbacksRequest) throws Exception{
		feedbacksService.submitFeedback(request, feedbacksRequest);
		return "OK";
	}
}
